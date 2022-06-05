package com.fresh.xy.sample.service.scan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.dto.scan.SampleScanAddDto;
import com.fresh.xy.sample.dto.scan.SampleScanSelDto;
import com.fresh.xy.sample.vo.scan.SampleScanVo;

public interface SampleScanService extends IService<SampleScan> {

    IPage<SampleScanVo> listByPojo(SampleScanSelDto scanSelDto);

    void saveAndRpc(SampleScanAddDto scanAddDto);

    void send(SampleScanAddDto scanDto);

    void sendCus();

    boolean txSend(SampleScanAddDto scanDto);

    boolean txSendCus(SampleScanAddDto scanDto);
}
