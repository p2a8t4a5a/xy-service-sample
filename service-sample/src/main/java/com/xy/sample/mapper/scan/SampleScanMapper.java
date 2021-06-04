package com.xy.sample.mapper.scan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.dto.scan.SampleScanSelDto;
import com.xy.sample.vo.scan.SampleScanVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

public interface SampleScanMapper extends BaseMapper<SampleScan> {

    IPage<SampleScanVo> selectByPojo(IPage<SampleScanVo> page, @Param("scanSelDto") SampleScanSelDto scanSelDto);
}
