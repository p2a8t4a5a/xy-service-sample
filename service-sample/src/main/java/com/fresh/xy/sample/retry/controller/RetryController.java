package com.fresh.xy.sample.retry.controller;

import com.fresh.common.exception.BizException;
import com.fresh.common.result.JsonResult;
import com.fresh.xy.sample.retry.FlRetrySample;
import com.fresh.xy.sample.dto.scan.SampleScanAddDto;
import com.fresh.xy.sample.dto.scan.SampleScanUpdateDto;
import com.fresh.xy.sample2.api.bo.Sample2ScanBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.retry.backoff.UniformRandomBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/retry/")
@Slf4j
public class RetryController {

    //https://github.com/spring-projects/spring-retry

    @Autowired
    private FlRetrySample flRetrySample;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("basicRetryWhenBizException")
    public JsonResult basicRetryWhenBizException() {
        String result = flRetrySample.basicRetryWhenBizException();
        return JsonResult.buildSuccessResult(result);
    }

    @GetMapping("retryTemplateWhenBizException")
    public JsonResult retryTemplateWhenBizException() {

        RetryTemplate retryTemplate = new RetryTemplate();

        //RetryPolicy
        Map<Class<? extends Throwable>, Boolean> expMap = new HashMap<>();
        expMap.put(BizException.class, true);
        SimpleRetryPolicy policy = new SimpleRetryPolicy(4, expMap);
        retryTemplate.setRetryPolicy(policy);

        //BackOffPolicy
        UniformRandomBackOffPolicy backOff = new UniformRandomBackOffPolicy();
        backOff.setMinBackOffPeriod(500);
        backOff.setMaxBackOffPeriod(1500);
        retryTemplate.setBackOffPolicy(backOff);

        //stateless(无状态的retry): retry, recover 发生在相同线程中，recover执行一次
        String result = retryTemplate.execute(context -> {
            log.info("retry thread: {}", Thread.currentThread().getId());
            throw new BizException(() -> "retryTemplate retry");
            //return "retry template";
        }, context -> {
            log.info("recover thread: {}", Thread.currentThread().getId());
            return "retryTemplate recover";
        });

        return JsonResult.buildSuccessResult(result);
    }


    @PostMapping("retrySave")
    public JsonResult retrySave(@RequestBody @Valid SampleScanAddDto scanAddDto) {
        boolean result = flRetrySample.retrySave(scanAddDto);
        if(result)
            return JsonResult.buildSuccessResult("retrySave success");
        else
            return JsonResult.buildFailedResult("retrySave failed");
    }

    @PostMapping("retryUpdate")
    public JsonResult retryUpdate(@RequestBody @Valid SampleScanUpdateDto scanUpdateDto) {
        boolean result = flRetrySample.retryUpdate(scanUpdateDto);
        if(result)
            return JsonResult.buildSuccessResult("retryUpdate success");
        else
            return JsonResult.buildFailedResult("retryUpdate failed");
    }

    @GetMapping("retryForRestTemplate")
    public JsonResult retryForRestTemplate(Long id) {
        /*JsonResult result = restTemplate.getForObject("http://service-sample2/sample2ScanRpc/getById?id=" + id,
                JsonResult.class);*/

        JsonResult<Sample2ScanBo> result = restTemplate.exchange("http://service-sample2/sample2ScanRpc/getById?id=" + id,
                HttpMethod.GET, null, new ParameterizedTypeReference<JsonResult<Sample2ScanBo>>() {
                }).getBody();

        return result;
    }


}
