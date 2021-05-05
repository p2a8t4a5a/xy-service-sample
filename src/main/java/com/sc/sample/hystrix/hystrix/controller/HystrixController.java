package com.sc.sample.hystrix.hystrix.controller;

import com.sc.common.vo.JsonResult;
import com.sc.sample.hystrix.FlHystrixCommandSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.Future;

@RestController
@RequestMapping("hystrix")
public class HystrixController {

    @Autowired
    private FlHystrixCommandSample flHystrixCommandSample;

    @GetMapping("command")
    public JsonResult hystrixCommand(Long id) {
        JsonResult result = flHystrixCommandSample.surroundByHystrix(id);

        return result;
    }

    @GetMapping("commandAsync")
    public JsonResult hystrixCommandAsync(Long id) {
        Future<JsonResult> resultFuture = flHystrixCommandSample.surroundByHystrixAsync(id);
        //do other things
        JsonResult result = null;
        try {
            result = resultFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailedResult(e.getMessage());
        }
        return result;
    }



}
