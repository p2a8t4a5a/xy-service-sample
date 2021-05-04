package com.sc.sample.api;

import com.sc.common.bo.ScanAddBo;
import com.sc.common.bo.ScanBo;
import com.sc.common.bo.ScanPageBo;
import com.sc.common.vo.BasicJsonResult;
import com.sc.common.vo.JsonResult;
import com.sc.common.vo.PageJsonResultVo;
import com.sc.sample.api.fallback.Sample2ServiceApiFallbackFac;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(name = "service-sample2", fallback = Sample2ServiceApiFallback.class)
@FeignClient(name = "service-sample2", fallbackFactory = Sample2ServiceApiFallbackFac.class)
public interface Sample2ServiceApi {

    @GetMapping("/scan2Api/get")
    BasicJsonResult<ScanBo> rpcGetById(@RequestParam("id") Long id);
    @GetMapping("/scan2Api/getByIds")
    BasicJsonResult<List<ScanBo>> rpcGetByIds(@RequestParam(name = "ids", required = false) List<Long> ids);
    @GetMapping("/scan2Api/listByPojo")
    BasicJsonResult<PageJsonResultVo<ScanBo>> rpcListByPojo(@SpringQueryMap ScanPageBo scanPageBo);
    @PostMapping("/scan2Api/save")
    JsonResult rpcSave(@RequestBody ScanAddBo scanAddBo);

}
