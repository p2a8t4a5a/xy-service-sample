package com.xy.sample.api.fallback;

import com.sc.common.bo.scan.SampleScanBo;
import com.sc.common.vo.JsonResult;
import com.xy.sample.api.SampleServiceApi;
import feign.hystrix.FallbackFactory;


public class SampleServiceApiFallback implements FallbackFactory<SampleServiceApi> {

    @Override
    public SampleServiceApi create(Throwable cause) {
        return new SampleServiceApi() {
            @Override
            public JsonResult<SampleScanBo> rpcGetById(Long id) {
                cause.printStackTrace();
                return JsonResult.buildFailedResult("rpcGetById调用失败,SampleService");
            }
        };
    }
}
