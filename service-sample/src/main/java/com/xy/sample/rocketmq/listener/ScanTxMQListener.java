package com.xy.sample.rocketmq.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sc.common.rmq.config.FlRmqTxProducer;
import com.sc.common.rmq.tx.FlRmqTxExecution;
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

@FlRmqTxProducer(producerName = "scanTxMQProducer", group = "sample-tx-scan-producer-group", corePoolSize = 2, maximumPoolSize = 2, keepAliveTime = 60000, blockingQueueSize = 2000)
public class ScanTxMQListener implements TransactionListener {
    private Logger log = LoggerFactory.getLogger(ScanTxMQListener.class);

    @Autowired
    private SampleScanService sampleScanService;

    @Transactional
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        SampleScanAddDto dto = (SampleScanAddDto) o;
        SampleScan scan = SampleScan.builder().name(dto.getName()).scanType(dto.getScanType()).scanTime(dto.getScanTime()).build();
        scan.setTxId(message.getTransactionId());
        sampleScanService.save(scan);
        /*try {//本地事务阻塞后，check可能先执行,TODO
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {

        try {
            SampleScan inserted = sampleScanService.getOne(new LambdaQueryWrapper<SampleScan>().select(SampleScan::getId).eq(SampleScan::getTxId, messageExt.getTransactionId()));
            if (inserted != null) return LocalTransactionState.COMMIT_MESSAGE;
        } catch (Exception e) {
            log.error("error while check, {}", e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
