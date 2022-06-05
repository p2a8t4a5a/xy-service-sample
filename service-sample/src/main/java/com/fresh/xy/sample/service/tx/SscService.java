package com.fresh.xy.sample.service.tx;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.xy.sample.entity.scan.SampleScan;

public interface SscService extends IService<SampleScan> {

    void nonTransactional();

    void transactional();
}
