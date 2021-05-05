package com.sc.sample.mapper.scan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sc.sample.dto.scan.SampleScanSelDto;
import com.sc.sample.entity.scan.SampleScan;
import com.sc.sample.vo.scan.SampleScanVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleScanMapper extends BaseMapper<SampleScan> {

    IPage<SampleScanVo> selectByPojo(IPage<SampleScanVo> page, @Param("scanSelDto") SampleScanSelDto scanSelDto);
}
