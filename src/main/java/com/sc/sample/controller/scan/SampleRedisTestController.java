package com.sc.sample.controller.scan;

import com.sc.common.vo.JsonResult;
import com.sc.sample.dto.redistest.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/sampleRedisTest/")
public class SampleRedisTestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("set")
    public JsonResult set(@RequestBody @Valid SampleRedisTestSetDto redisTestSetDto) {
        redisTemplate.opsForValue().set(redisTestSetDto.getKey(), redisTestSetDto.getValue());
        return JsonResult.buildSuccessResult("保存成功");
    }

    @PostMapping("setPojo")
    public JsonResult setPojo(@RequestBody @Valid SampleRedisTestSetPojoDto redisTestSetPojoDto) {
        PojoDto pojoDto = redisTestSetPojoDto.getPojoDto();
        Pojo2RedisDto pojo2RedisDto = Pojo2RedisDto.builder().name(pojoDto.getName()).pojoType(pojoDto.getPojoType().getValue()).pojoTime(pojoDto.getPojoTime()).build();
        redisTemplate.opsForValue().set(redisTestSetPojoDto.getKey(), pojo2RedisDto);
        return JsonResult.buildSuccessResult("保存成功");
    }

    @GetMapping("getPojo")
    public JsonResult getPojo(@RequestParam("key") String key) {
        Object result = redisTemplate.opsForValue().get(key);
        return JsonResult.buildSuccessResult(result);
    }

    @PostMapping("del")
    public JsonResult del(@RequestBody @Valid SampleRedisTestDelDto redisTestDelDto) {
        Boolean result = redisTemplate.delete(redisTestDelDto.getKey());
        if(result) return JsonResult.buildSuccessResult("删除成功,key="+redisTestDelDto.getKey());
        else return JsonResult.buildFailedResult("删除失败,key="+redisTestDelDto.getKey());
    }

    @GetMapping("get")
    public JsonResult get(@RequestParam("key") String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return JsonResult.buildSuccessResult(value);
    }



}
