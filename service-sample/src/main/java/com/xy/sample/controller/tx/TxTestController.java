package com.xy.sample.controller.tx;

import com.sc.common.vo.JsonResult;
import com.xy.sample.dto.scan.SampleScanAddDto;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.service.tx.TxTextService1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/txTest/")
public class TxTestController {

    @Autowired
    private TxTextService1 txTextService1;


    @PostMapping("txOne")
    public JsonResult txOne(@RequestBody @Valid SampleScanAddDto scanDto) {
        SampleScan scan = SampleScan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        txTextService1.txOne(scan);
        return JsonResult.buildSuccessResult("保存成功");
    }

    @PostMapping("txOne2")
    public JsonResult txOne2(@RequestBody @Valid SampleScanAddDto scanDto) {
        SampleScan scan = SampleScan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        txTextService1.txOne2(scan);
        return JsonResult.buildSuccessResult("保存成功");
    }


}
