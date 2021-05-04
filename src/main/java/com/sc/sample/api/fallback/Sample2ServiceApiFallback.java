package com.sc.sample.api.fallback;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sc.common.bo.ScanAddBo;
import com.sc.common.bo.ScanBo;
import com.sc.common.bo.ScanPageBo;
import com.sc.common.vo.BasicJsonResult;
import com.sc.common.vo.JsonResult;
import com.sc.common.vo.PageJsonResultVo;
import com.sc.sample.api.Sample2ServiceApi;
import com.sc.sample.entity.scan.Scan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/*@Component
@Slf4j*/
public class Sample2ServiceApiFallback implements Sample2ServiceApi {

    @Override
    public BasicJsonResult<ScanBo> rpcGetById(Long id) {
        //log.error("rpc调用失败id={}", id);
        return JsonResult.buildFailedBasicResult("rpcGetById调用失败,Sample2Service");
    }

    @Override
    public BasicJsonResult<List<ScanBo>> rpcGetByIds(List<Long> ids) {
        return JsonResult.buildFailedBasicResult("rpcGetByIds调用失败,Sample2Service");
    }

    @Override
    public BasicJsonResult<PageJsonResultVo<ScanBo>> rpcListByPojo(ScanPageBo scanPageBo) {
        return JsonResult.buildFailedBasicResult("rpcListByPojo调用失败,Sample2Service");
    }

    @Override
    public JsonResult rpcSave(ScanAddBo scanAddBo) {
        return JsonResult.buildFailedBasicResult("rpcSave调用失败,Sample2Service");
    }

}
