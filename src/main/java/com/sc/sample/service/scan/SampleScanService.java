package com.sc.sample.service.scan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.sample.dto.scan.SampleScanAddDto;
import com.sc.sample.dto.scan.SampleScanSelDto;
import com.sc.sample.entity.scan.SampleScan;
import com.sc.sample.vo.scan.SampleScanVo;

public interface SampleScanService extends IService<SampleScan> {

    IPage<SampleScanVo> listByPojo(SampleScanSelDto scanSelDto);

    void saveAndRpc(SampleScanAddDto scanAddDto);
}
