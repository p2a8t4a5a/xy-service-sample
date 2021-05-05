package com.sc.sample.api.fallback;

import com.sc.common.bo.ScanAddBo;
import com.sc.common.bo.ScanBo;
import com.sc.common.bo.ScanPageBo;
import com.sc.common.vo.BasicJsonResult;
import com.sc.common.vo.JsonResult;
import com.sc.common.vo.PageJsonResultVo;
import com.sc.sample.api.Sample2ServiceApi;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class Sample2ServiceApiFallbackFac implements FallbackFactory<Sample2ServiceApi> {
    @Override
    public Sample2ServiceApi create(Throwable throwable) {
        return new Sample2ServiceApi() {
            @Override
            public BasicJsonResult<ScanBo> rpcGetById(Long id) {
                throwable.printStackTrace();
                return JsonResult.buildFailedBasicResult("rpcGetById调用失败,Sample2Service");
            }

            @Override
            public BasicJsonResult<List<ScanBo>> rpcGetByIds(List<Long> ids) {
                throwable.printStackTrace();
                return JsonResult.buildFailedBasicResult("rpcGetByIds调用失败,Sample2Service");
            }

            @Override
            public BasicJsonResult<PageJsonResultVo<ScanBo>> rpcListByPojo(ScanPageBo scanPageBo) {
                throwable.printStackTrace();
                return JsonResult.buildFailedBasicResult("rpcListByPojo调用失败,Sample2Service");
            }

            @Override
            public BasicJsonResult<PageJsonResultVo<ScanBo>> rpcListByPojo2(ScanPageBo scanPageBo) {
                throwable.printStackTrace();
                return JsonResult.buildFailedBasicResult("rpcListByPojo2调用失败,Sample2Service");
            }

            @Override
            public JsonResult rpcSave(ScanAddBo scanAddBo) {
                throwable.printStackTrace();
                return JsonResult.buildFailedBasicResult("rpcSave调用失败,Sample2Service");
            }
        };
    }
}
