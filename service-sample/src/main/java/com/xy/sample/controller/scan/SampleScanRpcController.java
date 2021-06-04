package com.xy.sample.controller.scan;

import com.sc.common.bo.scan.SampleScanBo;
import com.sc.common.vo.JsonResult;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.service.scan.SampleScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对外提供rpc服务，路径中加上rpc的标记
 */
@Slf4j
@RestController
@RequestMapping("/sampleScanRpc/")
public class SampleScanRpcController {

    @Autowired
    private SampleScanService sampleScanService;

    @GetMapping("getById")
    public JsonResult getByIdRpc(@RequestParam(name = "id", required = false) Long id) {

        SampleScan scan = sampleScanService.getById(id);
        SampleScanBo scanBo = null;
        if(scan != null) {
            scanBo = SampleScanBo.builder().id(scan.getId()).name(scan.getName()).scanType(scan.getScanType())
                    .scanTime(scan.getScanTime())
                    .createTime(scan.getCreateTime())
                    .modifyTime(scan.getModifyTime())
                    .build();
        }
        return JsonResult.buildSuccessResult(scanBo);
    }



}
