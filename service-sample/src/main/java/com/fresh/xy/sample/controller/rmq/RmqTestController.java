package com.fresh.xy.sample.controller.rmq;

import com.fresh.common.result.JsonResult;
import com.fresh.xy.sample.service.scan.SampleScanService;
import com.fresh.xy.sample.dto.scan.SampleScanAddDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("rmqTest")
public class RmqTestController {

    @Autowired
    private SampleScanService sampleScanService;


    @PostMapping("send")
    public JsonResult send(@RequestBody @Valid SampleScanAddDto scanDto) {
        //使用默认 Producer发送
        sampleScanService.send(scanDto);
        return JsonResult.buildSuccessResult("send success");
    }

    @PostMapping("sendCus")
    public JsonResult sendCus() {
        //使用自定义 Producer发送
        sampleScanService.sendCus();
        return JsonResult.buildSuccessResult("sendCus success");
    }


    @PostMapping("txSend")
    public JsonResult txSend(@RequestBody @Valid SampleScanAddDto scanDto) {
        //使用默认 tx Producer发送
        try {
            boolean result = sampleScanService.txSend(scanDto);
            if(!result) return JsonResult.buildSuccessResult("txSend error");
        } catch (Exception e) {
            return JsonResult.buildSuccessResult("txSend error");
        }
        return JsonResult.buildSuccessResult("txSend success");
    }

    @PostMapping("txSendCus")
    public JsonResult txSendCus(@RequestBody @Valid SampleScanAddDto scanDto) {
        //使用自定义 tx Producer发送
        try {
            boolean result = sampleScanService.txSendCus(scanDto);
            if(!result) return JsonResult.buildSuccessResult("error");
        } catch (Exception e) {
            return JsonResult.buildSuccessResult("error");
        }
        return JsonResult.buildSuccessResult("保存成功");
    }



}
