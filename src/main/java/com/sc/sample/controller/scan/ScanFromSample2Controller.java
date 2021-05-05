package com.sc.sample.controller.scan;

import com.sc.common.bo.ScanAddBo;
import com.sc.common.bo.ScanBo;
import com.sc.common.bo.ScanPageBo;
import com.sc.common.enums.JsonResultEnum;
import com.sc.common.utils.AssertUtils;
import com.sc.common.vo.BasicJsonResult;
import com.sc.common.vo.JsonResult;
import com.sc.sample.api.Sample2ServiceApi;
import com.sc.sample.dto.scan.ScanSelDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/scanFromSample2/")
public class ScanFromSample2Controller {

    @Autowired
    private Sample2ServiceApi sample2ServiceApi;

    @GetMapping("get")
    public JsonResult get(Long id) {
        JsonResult result = sample2ServiceApi.rpcGetById(id);
        return result;
    }

    @GetMapping("getByIds")
    public JsonResult getByIds(@RequestParam(name = "ids", required = false) List<Long> ids) {
        JsonResult result = sample2ServiceApi.rpcGetByIds(ids);
        return result;
    }

    //Get请求的Enum中@JsonCreator
    @GetMapping("listByPojo")
    public JsonResult listByPojo(ScanPageBo scanPageBo) {
        AssertUtils.ifNull(scanPageBo, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanPageBo.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        JsonResult result = sample2ServiceApi.rpcListByPojo(scanPageBo);
        return result;
    }

    //compare with listByPojo, Get请求将参数放在请求体，使用@RequestBody解析请求体参数
    @GetMapping("listByPojo2")
    public JsonResult listByPojo2(@RequestBody ScanPageBo scanPageBo) {
        AssertUtils.ifNull(scanPageBo, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanPageBo.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        JsonResult result = sample2ServiceApi.rpcListByPojo2(scanPageBo);
        return result;
    }

    @PostMapping("save")
    public JsonResult save(@RequestBody @Valid ScanAddBo scanAddBo) {
        JsonResult result = sample2ServiceApi.rpcSave(scanAddBo);
        return result;
    }

}
