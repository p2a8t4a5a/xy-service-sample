package com.xy.sample.service.tx;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.sample.entity.scan.SampleScan;

public interface TxTextService1 extends IService<SampleScan> {
    void txOne(SampleScan scan);
    void txOne2(SampleScan scan);
    void txOne3(SampleScan scan);
    void txOne4(SampleScan scan);
}
