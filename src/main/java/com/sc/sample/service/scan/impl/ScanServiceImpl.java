package com.sc.sample.service.scan.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.sample.entity.scan.Scan;
import com.sc.sample.mapper.scan.ScanMapper;
import com.sc.sample.service.scan.ScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScanServiceImpl extends ServiceImpl<ScanMapper, Scan> implements ScanService {

}
