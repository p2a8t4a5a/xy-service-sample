package com.sc.sample.mapper.scan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sc.sample.dto.scan.ScanSelDto;
import com.sc.sample.entity.scan.Scan;
import com.sc.sample.vo.scan.ScanVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScanMapper extends BaseMapper<Scan> {

    IPage<ScanVo> selectByPojo(IPage<ScanVo> page, @Param("scanSelDto") ScanSelDto scanSelDto);
}
