package com.fresh.xy.sample.service.tx;

import com.fresh.xy.sample.entity.scan.SampleScan;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TxTextService1 extends IService<SampleScan> {
    void txOne(SampleScan scan);
    void txOne2(SampleScan scan);
    void txOne3(SampleScan scan);
    void txOne4(SampleScan scan);
}
