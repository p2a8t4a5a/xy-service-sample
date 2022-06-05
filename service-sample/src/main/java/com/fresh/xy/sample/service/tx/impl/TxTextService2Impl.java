package com.fresh.xy.sample.service.tx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.mapper.scan.SampleScanMapper;
import com.fresh.xy.sample.service.tx.TxTextService2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TxTextService2Impl extends ServiceImpl<SampleScanMapper, SampleScan> implements TxTextService2 {

    @Transactional
    @Override
    public void txTwo(SampleScan scan) {
        log.info("txTwo");
        scan.setId(null);
        scan.setName("txTwo_" + System.currentTimeMillis());
        this.save(scan);
        //throw new RuntimeException("some error");
    }

    @Transactional
    @Override
    public void txTwo2(SampleScan scan) {
        log.info("txTwo2");
        scan.setId(null);
        scan.setName("txTwo2_" + System.currentTimeMillis());
        this.save(scan);
        throw new RuntimeException("some error");
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void txTwo3(SampleScan scan) {
        log.info("txTwo3");
        scan.setId(null);
        scan.setName("txTwo3_" + System.currentTimeMillis());
        this.save(scan);
        //throw new RuntimeException("some error");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void txTwo4(SampleScan scan) {
        log.info("txTwo4");
        scan.setId(null);
        scan.setName("txTwo4_" + System.currentTimeMillis());
        this.save(scan);
        throw new RuntimeException("some error");
    }


    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void txTwo5(SampleScan scan) {
        log.info("txTwo5");
        scan.setId(null);
        scan.setName("txTwo5_" + System.currentTimeMillis());
        this.save(scan);
        //throw new RuntimeException("some error");
    }

    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void txTwo6(SampleScan scan) {
        log.info("txTwo6");
        scan.setId(null);
        scan.setName("txTwo6_" + System.currentTimeMillis());
        this.save(scan);
        throw new RuntimeException("some error");
    }

}
