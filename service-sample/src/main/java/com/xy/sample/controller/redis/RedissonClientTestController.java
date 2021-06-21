package com.xy.sample.controller.redis;


import com.sc.common.vo.JsonResult;
import com.sc.common.redis.dto.RedissonPojoDto;
import com.sc.common.redis.enums.PojoDtoEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/redissonTest/")
/*@RefreshScope*/
public class RedissonClientTestController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedissonReactiveClient redissonReactiveClient;

    /*@Value("${default.data}")
    private String cluster;*/

    @GetMapping("bucket")
    public JsonResult bucket() {
        //序列化器: FstCodec
        RBucket<Object> nullBucket = redissonClient.getBucket("NullBucket");
        nullBucket.set(null);
        Object nullObj = nullBucket.get();

        RBucket<Boolean> booleanBucket = redissonClient.getBucket("BooleanBucket");
        booleanBucket.set(true);
        Boolean booleanObj = booleanBucket.get();
        booleanBucket.set(false);
        booleanObj = booleanBucket.get();

        RBucket<Double> doubleBucket = redissonClient.getBucket("DoubleBucket");
        doubleBucket.set(2.332131231212445645674765756877867897853764645645645656546546456);
        Double doubleObj = doubleBucket.get();

        BigDecimal bd = new BigDecimal("8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423");
        RBucket<BigDecimal> bigDecimalBucket = redissonClient.getBucket("BigDecimalBucket");
        bigDecimalBucket.set(bd);
        BigDecimal bdObj = bigDecimalBucket.get();

        RBucket<String> stringBucket = redissonClient.getBucket("StringBucket");
        stringBucket.set("pojo1");
        String stringObj = stringBucket.get();

        RBucket<LocalDateTime> localDateTimeBucket = redissonClient.getBucket("LocalDateTimeBucket");
        localDateTimeBucket.set(LocalDateTime.now());
        LocalDateTime localDateTimeObj = localDateTimeBucket.get();

        RedissonPojoDto pojo = RedissonPojoDto.builder()
                .id(1234534535354L)
                .bl(false)
                .s(null)
                .name("just pojo哒哒哒")
                .bd(bd)
                .pojoType(PojoDtoEnum.SYSTEM.getValue())
                .pojoTime(LocalDateTime.now())
                .build();
        RBucket<RedissonPojoDto> pojoBucket = redissonClient.getBucket("PojoBucket");
        pojoBucket.set(pojo);
        RedissonPojoDto pojoObj = pojoBucket.get();


        List<RedissonPojoDto> list = new ArrayList<>();
        list.add(pojo);
        RBucket<List<RedissonPojoDto>> pojoListBucket = redissonClient.getBucket("PojoListBucket");
        pojoListBucket.set(list);
        List<RedissonPojoDto> pojoListObj = pojoListBucket.get();


        Map<String, RedissonPojoDto> map = new HashMap<>();
        map.put("pojo1", pojo);
        RBucket<Map<String, RedissonPojoDto>> pojoMapBucket = redissonClient.getBucket("PojoMapBucket");
        pojoMapBucket.set(map);
        Map<String, RedissonPojoDto> pojoMapObj = pojoMapBucket.get();

        return JsonResult.buildSuccessResult(pojo);
    }


    @GetMapping("lock")
    public JsonResult lock() {
        RLock lock = redissonClient.getLock("myLock");
        try {
            lock.lock();  //尝试获取锁，如果锁不可用，当前线程处于休眠状态,直到当前线程获取锁
            //获取锁之后，锁leaseTime取Config.lockWatchdogTimeout的值
            //Watch Dog会自动续期: 每隔30/3=10秒续到30秒
        } finally {
            lock.unlock();//手动解锁
        }

        return JsonResult.buildSuccessResult("success");
    }



}
