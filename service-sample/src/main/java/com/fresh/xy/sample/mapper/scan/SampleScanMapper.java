package com.fresh.xy.sample.mapper.scan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fresh.xy.sample.dto.scan.SampleScanSelDto;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.vo.scan.SampleScanVo;
import org.apache.ibatis.annotations.Param;

public interface SampleScanMapper extends BaseMapper<SampleScan> {

    IPage<SampleScanVo> selectByPojo(IPage<SampleScanVo> page, @Param("scanSelDto") SampleScanSelDto scanSelDto);
}
