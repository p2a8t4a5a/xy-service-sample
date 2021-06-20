package com.xy.sample.service.scan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.dto.scan.SampleScanAddDto;
import com.xy.sample.dto.scan.SampleScanSelDto;
import com.xy.sample.vo.scan.SampleScanVo;

public interface SampleScanService extends IService<SampleScan> {

    IPage<SampleScanVo> listByPojo(SampleScanSelDto scanSelDto);

    void saveAndRpc(SampleScanAddDto scanAddDto);

    void sampleTopicUpdate(SampleScanAddDto scanDto);

    boolean txProducer(SampleScanAddDto scanDto);

    boolean txProducer2(SampleScanAddDto scanDto);

    void other();


}
