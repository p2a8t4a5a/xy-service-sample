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

    void send(SampleScanAddDto scanDto);

    void sendCus();

    boolean txSend(SampleScanAddDto scanDto);

    boolean txSendCus(SampleScanAddDto scanDto);
}
