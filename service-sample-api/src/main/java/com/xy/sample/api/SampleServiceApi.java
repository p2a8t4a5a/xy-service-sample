package com.xy.sample.api;

import com.sc.common.bo.scan.SampleScanBo;
import com.sc.common.vo.JsonResult;
import com.xy.sample.api.fallback.SampleServiceApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "service-sample", fallbackFactory = SampleServiceApiFallback.class)
public interface SampleServiceApi {

    @GetMapping("/sampleScanRpc/getById")
    JsonResult<SampleScanBo> rpcGetById(@RequestParam("id") Long id);

}
