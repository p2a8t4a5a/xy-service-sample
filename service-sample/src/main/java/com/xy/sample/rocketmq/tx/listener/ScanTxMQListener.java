package com.xy.sample.rocketmq.tx.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sc.common.rmq.tx.entity.RmqTx;
import com.sc.common.rmq.config.FlRmqTxProducer;
import com.sc.common.rmq.tx.service.RmqTxService;
import com.xy.sample.dto.scan.SampleScanAddDto;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.service.scan.SampleScanService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@FlRmqTxProducer(producerName = "scanTxMQProducer", group = "sample-tx-scan-producer-group", corePoolSize = 1, maximumPoolSize = 1, keepAliveTime = 50000, blockingQueueSize = 1000)
public class ScanTxMQListener implements TransactionListener {
    private Logger log = LoggerFactory.getLogger(ScanTxMQListener.class);

    @Autowired
    private SampleScanService sampleScanService;

    @Autowired
    private RmqTxService rmqTxService;

    @Transactional
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        SampleScanAddDto dto = (SampleScanAddDto) o;

        SampleScan scan = SampleScan.builder().name(dto.getName()).scanType(dto.getScanType()).scanTime(dto.getScanTime()).build();
        sampleScanService.save(scan);
        //int i=1/0;
        RmqTx rmqTx = RmqTx.builder().txId(message.getTransactionId()).status(0).build();
        rmqTxService.save(rmqTx);

        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        String txId = messageExt.getTransactionId();
        try {
            RmqTx rmqTx = rmqTxService.getOne(new LambdaQueryWrapper<RmqTx>().select(RmqTx::getId).eq(RmqTx::getTxId, txId));
            if(rmqTx != null) {
                try {
                    rmqTxService.update(new LambdaUpdateWrapper<RmqTx>().set(RmqTx::getStatus, 1).eq(RmqTx::getId, rmqTx.getId()));
                } catch (Exception e) {
                    log.warn("事务回查时更新check_status失败, txId={}", txId);
                }
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        } catch (Exception e) {
            log.error("事务回查失败, txId={}", txId);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
