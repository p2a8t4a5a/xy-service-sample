package com.xy.sample.rocketmq.tx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sc.common.rmq.tx.entity.RmqTx;
import com.sc.common.rmq.tx.RmqTxListenerService;
import com.sc.common.rmq.tx.service.RmqTxService;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.rocketmq.tx.model.ScanRmqTxModel;
import com.xy.sample.service.scan.SampleScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component("scanTmqTxService")
public class ScanRmqTxListenerServiceImpl implements RmqTxListenerService<ScanRmqTxModel> {

    @Autowired
    private SampleScanService sampleScanService;

    @Autowired
    private RmqTxService rmqTxService;

    @Transactional
    @Override
    public boolean executeLocalTx(ScanRmqTxModel model) {
        SampleScan scan = SampleScan.builder().name(model.getName()).scanType(model.getScanType()).scanTime(model.getScanTime()).build();
        sampleScanService.save(scan);
        //int i = 1/0;
        RmqTx rmqTx = RmqTx.builder().txId(model.getTxId()).checkStatus(0).build();
        rmqTxService.save(rmqTx);
        /*try {//本地事务阻塞后，check可能先执行,TODO
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return true;
    }

    @Override
    public boolean checkLocalTx(String txId) {
        try {
            RmqTx rmqTx = rmqTxService.getOne(new LambdaQueryWrapper<RmqTx>().select(RmqTx::getId).eq(RmqTx::getTxId, txId));
            if(rmqTx != null) {
                try {
                    rmqTxService.update(new LambdaUpdateWrapper<RmqTx>().set(RmqTx::getCheckStatus, 1).eq(RmqTx::getId, rmqTx.getId()));
                } catch (Exception e) {
                    log.warn("事务回查时更新check_status失败, txId={}", txId);
                }
                return true;
            }
        } catch (Exception e) {
            log.error("事务回查失败, txId={}, exception={}", txId, e.getMessage());
            return false;
        }
        return false;
    }
}
