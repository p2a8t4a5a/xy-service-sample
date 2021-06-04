package com.xy.sample.api.fallback;

import com.sc.common.bo.scan.Sample2ScanAddBo;
import com.sc.common.bo.scan.Sample2ScanBo;
import com.sc.common.bo.scan.Sample2ScanPageBo;
import com.sc.common.enums.ScanTypeEnum;
import com.sc.common.vo.JsonResult;
import com.sc.common.vo.PageJsonResultVo;
import com.xy.sample.api.Sample2ServiceApi;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class Sample2ServiceApiFallbackFac implements FallbackFactory<Sample2ServiceApi> {
    @Override
    public Sample2ServiceApi create(Throwable throwable) {
        return new Sample2ServiceApi() {
            @Override
            public JsonResult<Sample2ScanBo> rpcGetById(Long id) {
                throwable.printStackTrace();
                return JsonResult.buildFailedResult("rpcGetById调用失败,Sample2Service");
            }

            @Override
            public JsonResult<List<Sample2ScanBo>> rpcGetByIds(List<Long> ids) {
                throwable.printStackTrace();
                return JsonResult.buildFailedResult("rpcGetByIds调用失败,Sample2Service");
            }

            @Override
            public JsonResult<PageJsonResultVo<Sample2ScanBo>> rpcListByPojo(Sample2ScanPageBo scanPageBo) {
                throwable.printStackTrace();
                return JsonResult.buildFailedResult("rpcListByPojo调用失败,Sample2Service");
            }

            @Override
            public JsonResult<PageJsonResultVo<Sample2ScanBo>> rpcListByPojo2(Sample2ScanPageBo scanPageBo) {
                throwable.printStackTrace();
                return JsonResult.buildFailedResult("rpcListByPojo2调用失败,Sample2Service");
            }

            @Override
            public JsonResult<PageJsonResultVo<Sample2ScanBo>> rpcListByParams(Long id, ScanTypeEnum scanType, LocalDateTime scanTime, Long page, Long pageSize) {
                throwable.printStackTrace();
                return JsonResult.buildFailedResult("rpcListByParams调用失败,Sample2Service");
            }

            @Override
            public JsonResult rpcSave(Sample2ScanAddBo scanAddBo) {
                throwable.printStackTrace();
                return JsonResult.buildFailedResult("rpcSave调用失败,Sample2Service");
            }
        };
    }
}
