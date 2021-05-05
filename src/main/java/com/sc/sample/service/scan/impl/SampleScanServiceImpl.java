package com.sc.sample.service.scan.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.common.bo.Sample2ScanAddBo;
import com.sc.common.exception.BizException;
import com.sc.common.vo.JsonResult;
import com.sc.sample.api.Sample2ServiceApi;
import com.sc.sample.dto.scan.SampleScanAddDto;
import com.sc.sample.dto.scan.SampleScanSelDto;
import com.sc.sample.entity.scan.SampleScan;
import com.sc.sample.mapper.scan.SampleScanMapper;
import com.sc.sample.service.scan.SampleScanService;
import com.sc.sample.vo.scan.SampleScanVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.tools.rmi.Sample;
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
