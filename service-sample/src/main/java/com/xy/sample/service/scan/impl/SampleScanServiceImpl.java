package com.xy.sample.service.scan.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.common.bo.scan.Sample2ScanAddBo;
import com.sc.common.exception.BizException;
import com.sc.common.vo.JsonResult;
import com.xy.sample.mapper.scan.SampleScanMapper;
import com.xy.sample.dto.scan.SampleScanAddDto;
import com.xy.sample.dto.scan.SampleScanSelDto;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.service.scan.SampleScanService;
import com.xy.sample.vo.scan.SampleScanVo;
import com.xy.sample2.api.Sample2ServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SampleScanServiceImpl extends ServiceImpl<SampleScanMapper, SampleScan> implements SampleScanService {

    @Autowired
    private SampleScanMapper sampleScanMapper;

    @Autowired
    private Sample2ServiceApi sample2ServiceApi;

    @Override
    public IPage<SampleScanVo> listByPojo(SampleScanSelDto scanSelDto) {
        IPage<SampleScanVo> page = scanSelDto.buildPage(SampleScanVo.class);
        sampleScanMapper.selectByPojo(page, scanSelDto);
        return page;
    }

    @Transactional
    @Override
    public void saveAndRpc(SampleScanAddDto scanAddDto) {
        SampleScan sampleScan = SampleScan.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        this.save(sampleScan);
        Sample2ScanAddBo scanAddBo = Sample2ScanAddBo.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        JsonResult result = sample2ServiceApi.rpcSave(scanAddBo);
        if(!result.getSuccess())
            throw new BizException(() -> "rpc 保存失败");

    }
}
