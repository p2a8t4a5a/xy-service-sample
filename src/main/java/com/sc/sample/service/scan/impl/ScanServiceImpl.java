package com.sc.sample.service.scan.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.sample.dto.scan.ScanSelDto;
import com.sc.sample.entity.scan.Scan;
import com.sc.sample.mapper.scan.ScanMapper;
import com.sc.sample.service.scan.ScanService;
import com.sc.sample.vo.scan.ScanVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ScanServiceImpl extends ServiceImpl<ScanMapper, Scan> implements ScanService {

    @Autowired
    private ScanMapper scanMapper;

    @Override
    public IPage<ScanVo> listByPojo(ScanSelDto scanSelDto) {
        IPage<ScanVo> page = scanSelDto.buildPage(ScanVo.class);
        scanMapper.selectByPojo(page, scanSelDto);
        return page;
    }
}
