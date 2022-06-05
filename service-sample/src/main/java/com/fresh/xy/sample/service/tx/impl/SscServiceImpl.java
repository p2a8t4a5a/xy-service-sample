package com.fresh.xy.sample.service.tx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.mapper.scan.SampleScanMapper;
import com.fresh.xy.sample.service.tx.SscService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SscServiceImpl extends ServiceImpl<SampleScanMapper, SampleScan> implements SscService {

    @Autowired
    private SampleScanMapper sampleScanMapper;


    @Override
    public void nonTransactional() {

        sampleScanMapper.selectById(1464205512272723970L);
        sampleScanMapper.selectById(1464205512272723970L);

    }

    @Transactional
    @Override
    public void transactional() {

        sampleScanMapper.selectById(1464205512272723970L);
        sampleScanMapper.selectById(1464205512272723970L);


    }
}
