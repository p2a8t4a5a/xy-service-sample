package com.sc.sample.service.scan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sc.sample.dto.scan.ScanSelDto;
import com.sc.sample.entity.scan.Scan;
import com.sc.sample.vo.scan.ScanVo;

public interface ScanService extends IService<Scan> {

    IPage<ScanVo> listByPojo(ScanSelDto scanSelDto);
}
