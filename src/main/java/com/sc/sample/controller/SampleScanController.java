package com.sc.sample.controller;

import com.sc.common.scan.ScanUtils;
import com.sc.common.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sampleScan/")
public class SampleScanController {

    @GetMapping("listAll")
    public JsonResult listAll() {
        return JsonResult.buildSuccessResult(ScanUtils.scan("service-sample"));
    }

}
