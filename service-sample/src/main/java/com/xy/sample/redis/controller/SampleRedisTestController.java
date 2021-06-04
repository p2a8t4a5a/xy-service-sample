package com.xy.sample.redis.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.sc.common.redis.FlCustomSerializer;
import com.sc.common.utils.ReflectUtils;
import com.sc.common.vo.JsonResult;
import com.xy.sample.redis.dto.Pojo2RedisDto;
import com.xy.sample.redis.dto.SampleRedisTestDelDto;
import com.xy.sample.redis.enums.PojoDtoEnum;
import com.xy.sample.serialize.FlSerializeTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sampleRedisTest/")
public class SampleRedisTestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, Number> numberRedisTemplate;

    //@Autowired
    //private RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, String> customRedisTemplate;

    @Autowired
    private FlCustomSerializer flCustomSerializer;

    @Autowired
    private RedisTemplate<String, byte[]> genericRedisTemplate;


    /**
     * 一种操作方法即
     *  使用RedisTemplate<String, String>,value,list-value,hash-value均当作String
     *  "将序列化和反序列化外放"（此为核心思想）:
     *   1.将对象序列化成String: 使用ObjectMapper将任意对象序列化成String
     *   2.使用stringRedisTemplate写到redis: 使用StringRedisSerializer(默认UTF-8编码)将String根据UTF-8转化为byte[],将byte[]发送给redis
     *   3.使用stringRedisTemplate从redis读,保存为String: 从redis读出byte[], 使用StringRedisSerializer(默认UTF-8编码)将byte[]根据UTF-8转化为String
     *   4.将String反序列化成对象: 使用ObjectMapper将String反序列化成想要的类型
     *  note: 此种操作和"另一种"操作是兼容的(需统一编码): @see test1(), test2()
     *
     */
    @PostMapping("setString")
    public JsonResult setString() {

        Integer somethingNull = null;
        customRedisTemplate.opsForValue().set("Null", flCustomSerializer.serialize(somethingNull));//序列化为 "", redis中保存为 "Null"->""
        String nullString = customRedisTemplate.opsForValue().get("Null");//从redis取出，保存为 ""
        Object nullVal = flCustomSerializer.deserialize(nullString);//反序列化为 null
        Integer nullVal2 = flCustomSerializer.deserialize(nullString, Integer.class);//反序列化为 null


        customRedisTemplate.opsForValue().set("Boolean", flCustomSerializer.serialize(true));//序列化为 "true", redis中保存为 "Boolean"->"true"
        String booleanString = customRedisTemplate.opsForValue().get("Boolean");   //从redis取出，保存为 "true"
        Boolean booleanVal = flCustomSerializer.deserialize(booleanString, Boolean.class);//反序列化为 Boolean:true


        customRedisTemplate.opsForValue().set("Character", flCustomSerializer.serialize('a'));//序列化为 "\"a\"", redis中保存为 "Character"->"\"a\""
        String charString = customRedisTemplate.opsForValue().get("Character");    //从redis取出，保存为 "\"a\""
        Character charVal = flCustomSerializer.deserialize(charString, Character.class);//反序列化为 Character:'a'


        customRedisTemplate.opsForValue().set("Byte", flCustomSerializer.serialize((byte) 1));//序列化为 "1", redis中保存为 "Byte"->"1"
        String byteString = customRedisTemplate.opsForValue().get("Byte");    //从redis取出，保存为 "1"
        Byte byteVal = flCustomSerializer.deserialize(byteString, Byte.class);//反序列化为 Byte:1


        customRedisTemplate.opsForValue().set("Short", flCustomSerializer.serialize((short) 2));//序列化为 "2", redis中保存为 "Short"->"2"
        String shortString = customRedisTemplate.opsForValue().get("Short");  //从redis取出，保存为 "2"
        Short shortVal = flCustomSerializer.deserialize(shortString, Short.class);//反序列化为 Short:2


        customRedisTemplate.opsForValue().set("Integer", flCustomSerializer.serialize(3));//序列化为 "3", redis中保存为 "Integer"->"3"
        String integerString = customRedisTemplate.opsForValue().get("Integer");//从redis取出，保存为 "3"
        Integer integerVal = flCustomSerializer.deserialize(integerString, Integer.class);//反序列化为 Integer:3


        customRedisTemplate.opsForValue().set("Long", flCustomSerializer.serialize(1234567890987666L));//序列化为 "1234567890987666", redis保存为 "Long"->"1234567890987666"
        String longString = customRedisTemplate.opsForValue().get("Long");//从redis取出，保存为"1234567890987666"
        Long longVal = flCustomSerializer.deserialize(longString, Long.class);//反序列化为 Long:1234567890987666L


        customRedisTemplate.opsForValue().set("String", flCustomSerializer.serialize("123中"));//序列化为 "\"123中\"", redis保存为 "String"->"\"123\xe4\xb8\xad\""
        String stringString = customRedisTemplate.opsForValue().get("String");//从redis取出，保存为 "\"123中\""
        String stringVal = flCustomSerializer.deserialize(stringString, String.class);//反序列化为 String:"123中"


        customRedisTemplate.opsForValue().set("Float", flCustomSerializer.serialize(1.2345666666666666666675465645f));//序列化为 "1.2345667", redis保存为 "Float"->"1.2345667"
        String floatString = customRedisTemplate.opsForValue().get("Float");//从redis取出，保存为 "1.2345667"
        Float floatVal = flCustomSerializer.deserialize(floatString, Float.class);//反序列化为 Float


        customRedisTemplate.opsForValue().set("Double", flCustomSerializer.serialize(121.321312312312312323123123123131231));//序列化为 "121.32131231231232", redis保存为 "Double"->"121.32131231231232"
        String doubleString = customRedisTemplate.opsForValue().get("Double");//从redis取出，保存为 "121.32131231231232"
        Double doubleVal = flCustomSerializer.deserialize(doubleString, Double.class);//反序列化为 Double

        BigInteger bi = new BigInteger("771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121");
        BigDecimal bd = new BigDecimal("8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423");

        customRedisTemplate.opsForValue().set("BigInteger", flCustomSerializer.serialize(bi));//序列化为 "["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121]"
        String bigIntegerString = customRedisTemplate.opsForValue().get("BigInteger");//从redis取出，保存为 "["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121]"
        BigInteger biVal = flCustomSerializer.deserialize(bigIntegerString, BigInteger.class);//反序列化为BigInteger

        customRedisTemplate.opsForValue().set("BigDecimal", flCustomSerializer.serialize(bd));//序列化为 "["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        String bigDecimalString = customRedisTemplate.opsForValue().get("BigDecimal");//从redis取出，保存为 "["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        BigDecimal bdVal = flCustomSerializer.deserialize(bigDecimalString, BigDecimal.class);//反序列化为BigDecimal


        customRedisTemplate.opsForValue().set("Enum", flCustomSerializer.serialize(PojoDtoEnum.SYSTEM.getValue()));//将Enum的value字段(String类型)序列化为 "\"SYSTEM\"", redis保存为 "Enum"->"\"SYSTEM\""
        String enumString = customRedisTemplate.opsForValue().get("Enum");//从redis取出，保存为 "\"SYSTEM\""
        PojoDtoEnum enumVal = PojoDtoEnum.getByValue(flCustomSerializer.deserialize(enumString, String.class));//反序列化并使用该String构造Enum


        customRedisTemplate.opsForValue().set("LocalDateTime", flCustomSerializer.serialize(LocalDateTime.now()));//序列化为 "\"2021-05-16 22:05:41\"", redis保存为 "LocalDateTime"->"\"2021-05-16 22:05:41\""
        String localDateTimeString = customRedisTemplate.opsForValue().get("LocalDateTime");//从redis取出，保存为 "\"2021-05-16 22:05:41\""
        LocalDateTime localDateTimeVal = flCustomSerializer.deserialize(localDateTimeString, LocalDateTime.class);//反序列为LocalDateTime


        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("q中");
        customRedisTemplate.opsForValue().set("List", flCustomSerializer.serialize(list));//序列化为 "["java.util.ArrayList",["1","q中"]]", redis保存为 "List"->"[\"java.util.ArrayList\",[\"1\",\"q\xe4\xb8\xad\"]]"
        String listString = customRedisTemplate.opsForValue().get("List");//从redis取出, 保存为 "["java.util.ArrayList",["1","q中"]]"
        List<String> listVal = flCustomSerializer.deserialize(listString, new TypeReference<List<String>>(){}); //反序列化为List<String>


        //对每一个list-value序列化，对Long序列化成 "4123123122", 对String序列化成 "\"中\"", 对BigDecimal序列化成 "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        //                                  对Enum的String序列化成 "\"SYSTEM\"", 对LocalDateTime序列化成 "\"2021-05-16 14:03:19\""
        //redis中保存为
        //      127.0.0.1:6379> lrange list-value 0 5
        //      1) "\"2021-05-16 14:03:19\""
        //      2) "\"SYSTEM\""
        //      3) "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        //      4) "\"\xe4\xb8\xad\""
        //      5) "4123123122"
        customRedisTemplate.delete("list-value");
        customRedisTemplate.opsForList().leftPushAll("list-value", flCustomSerializer.serialize(4123123122L),
                flCustomSerializer.serialize("中"), flCustomSerializer.serialize(bd), flCustomSerializer.serialize(PojoDtoEnum.SYSTEM.getValue()),
                flCustomSerializer.serialize(LocalDateTime.now()));
        Long length = customRedisTemplate.opsForList().size("list-value");
        //从redis取出，保存为List<String>: "\"2021-05-16 14:03:19\"", "\"SYSTEM\"", "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]", "\"中\"", "4123123122"
        List<String> listValues = customRedisTemplate.opsForList().range("list-value", 0, length);
        //对每一个值反序列化...


        //对每一个hash-value序列化，对Enum的String序列化成 "\"SYSTEM\"", 对LocalDateTime序列化成 "\"2021-05-16 17:02:34\"", 对BigDecimal序列化成 "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]",
        //                                  对String序列化成 "\"中\"", 对Long序列化成 "124234252453453"
        //redis中保存为
        //      127.0.0.1:6381> hgetall hash-value
        //       "key5"->"124234252453453",
        //       "key2"->"\"2021-05-16 17:02:34\"",
        //       "key3"->"[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]",
        //       "key4"->"\"\xe4\xb8\xad\"",
        //       "key1"->"\"SYSTEM\""
        customRedisTemplate.opsForHash().put("hash-value", "key1", flCustomSerializer.serialize(PojoDtoEnum.SYSTEM.getValue()));
        customRedisTemplate.opsForHash().put("hash-value", "key2", flCustomSerializer.serialize(LocalDateTime.now()));
        customRedisTemplate.opsForHash().put("hash-value", "key3", flCustomSerializer.serialize(bd));
        customRedisTemplate.opsForHash().put("hash-value", "key4", flCustomSerializer.serialize("中"));
        customRedisTemplate.opsForHash().put("hash-value", "key5", flCustomSerializer.serialize(124234252453453L));

        String hashKey1 = (String) customRedisTemplate.opsForHash().get("hash-value", "key1");//从redis取出，保存为"\"SYSTEM\""
        String hashKey1Val = flCustomSerializer.deserialize(hashKey1, String.class);//反序列化为String: "SYSTEM"

        String hashKey2 = (String) customRedisTemplate.opsForHash().get("hash-value", "key2");//从redis取出，保存为"\"2021-05-16 17:02:34\""
        LocalDateTime hashKey2Val = flCustomSerializer.deserialize(hashKey2, LocalDateTime.class);//反序列化为LocalDateTime

        String hashKey3 = (String) customRedisTemplate.opsForHash().get("hash-value", "key3");//从redis取出，保存为"[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        BigDecimal hashKey3Val = flCustomSerializer.deserialize(hashKey3, BigDecimal.class);//反序列化为BigDecimal

        String hashKey4 = (String) customRedisTemplate.opsForHash().get("hash-value", "key4");//从redis取出，保存为"\"中\""
        String hashKey4Val = flCustomSerializer.deserialize(hashKey4, String.class);//反序列化为 String: "中"

        String hashKey5 = (String) customRedisTemplate.opsForHash().get("hash-value", "key5");//从redis取出，保存为"124234252453453"
        Long hashKey5Val = flCustomSerializer.deserialize(hashKey5, Long.class);//反序列化为 Long


        Pojo2RedisDto pojo2RedisDto = Pojo2RedisDto.builder()
                .id(1234534535354L)
                .bl(false)
                .s(null)
                .name("just pojo哒哒哒")
                .bi(bi)
                .bd(bd)
                .pojoType(PojoDtoEnum.SYSTEM.getValue())
                .pojoTime(LocalDateTime.now())
                .build();
        //1.序列化Pojo
        //"["com.sc.sample.redis.dto.Pojo2RedisDto",{"id":1234534535354,"bl":false,"s":null,"name":"just pojo哒哒哒","bi":["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],"bd":["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],"pojoType":"SYSTEM","pojoTime":"2021-05-17 22:30:39"}]"
        String se = flCustomSerializer.serialize(pojo2RedisDto);
        //2.保存到redis
        //"[\"com.sc.sample.redis.dto.Pojo2RedisDto\",{\"id\":1234534535354,\"bl\":false,\"s\":null,\"name\":\"just pojo\xe5\x93\x92\xe5\x93\x92\xe5\x93\x92\",\"bi\":[\"java.math.BigInteger\",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],\"bd\":[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],\"pojoType\":\"SYSTEM\",\"pojoTime\":\"2021-05-17 22:30:39\"}]"
        customRedisTemplate.opsForValue().set("Pojo", se);
        //3.从redis取出，保存为String
        //"["com.sc.sample.redis.dto.Pojo2RedisDto",{"id":1234534535354,"bl":false,"s":null,"name":"just pojo哒哒哒","bi":["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],"bd":["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],"pojoType":"SYSTEM","pojoTime":"2021-05-17 22:30:39"}]"
        String result = customRedisTemplate.opsForValue().get("Pojo");
        //4.反序列化为Pojo
        Pojo2RedisDto pojoDe = flCustomSerializer.deserialize(result, Pojo2RedisDto.class);


        return JsonResult.buildSuccessResult(pojoDe);
    }




    /**
     * 另一种操作方法为
     *  使用RedisTemplate<String, Object>,value,list-value,hash-value均是Object类型，均使用Jackson2JsonRedisSerializer:
     *  1.使用RedisTemplate写到redis: 使用Jackson2JsonRedisSerializer将对象序列化成byte[](如果序列化器为null并且已经是byte[]，就直接使用该byte[]),将byte[]发送给redis
     *  2.使用RedisTemplate从redis读: 从redis读出byte[], 使用Jackson2JsonRedisSerializer将byte[]反序列化成对象
     *
     *  Jackson2JsonRedisSerializer使用ObjectMapper序列化和反序列化
     *  1.序列化: 将对象序列化成byte[]（统一编码设置为使用UTF-8，做到和"一种"操作兼容 see test1(), test2()）
     *  2.反序列化:
     *   a、ObjectMapper反序列化时调用的是 Object obj = readValue(..., Object.class)，内部将使用UntypedObjectDeserializer反序列化器，该反序列化器
     *     表现不尽如人意(因为不知道具体的类型，所以没办法准确的反序列成对应的对象)，如一个json对象串返回LinkedHashMap,小数返回Double而丢失精度等
     *   b、设置objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);从而将对象类型信息保存到序列化的字符串中
     *     存储格式，反序列化的表现 {@link FlSerializeTest}
     *
     *
     */
    @PostMapping("setObject")
    public JsonResult setObject() {

        Object somethingNull = null;
        redisTemplate.opsForValue().set("Null", somethingNull);//ObjectMapper将此Boolean序列化成 byte[0], redis中保存为 "Null"->""
        Object nullObject = redisTemplate.opsForValue().get("Null");//从redis取出 ""的byte[] 反序列化，实际为null

        redisTemplate.opsForValue().set("Boolean", true);//ObjectMapper将此Boolean序列化成 "true"的byte[], redis中保存为 "Boolean"->"true"
        Object booleanObject = redisTemplate.opsForValue().get("Boolean");//从redis取出 "true"的byte[] 反序列化，实际类型是 Boolean:true


        redisTemplate.opsForValue().set("Character", 'a');//ObjectMapper将此Char序列化成 "\"a\""的byte[], redis中保存为 "Character"->"\"a\""
        Object charObject = redisTemplate.opsForValue().get("Character");//从redis取出 "\"a\""的byte[] 反序列化，实际类型是 String:"a"


        redisTemplate.opsForValue().set("Byte", (byte) 1);//ObjectMapper将此Byte序列化成 "1"的byte[], redis中保存为 "Byte"->"1"
        Object byteObject = redisTemplate.opsForValue().get("Byte");//从redis取出 "1"的byte[] 反序列化，实际类型是 Integer:1

        redisTemplate.opsForValue().set("Short", (short) 2);//ObjectMapper将此Short序列化成 "2"的byte[], redis中保存为 "Short"->"2"
        Object shortObject = redisTemplate.opsForValue().get("Short");//从redis取出 "2"的byte[] 反序列化，实际类型是 Integer: 2

        redisTemplate.opsForValue().set("Integer", 3);//ObjectMapper将此Integer序列化成 "3"的byte[], redis中保存为 "Integer"->"3"
        Object integerObject = redisTemplate.opsForValue().get("Integer");//从redis取出 "3"的byte[] 反序列化，实际类型是 Integer:3

        redisTemplate.opsForValue().set("Long", 1234567890987666L);//ObjectMapper将此Long序列化成 "1234567890987666"的byte[], redis中保存为 "Long"->"1234567890987666"
        Object longObject = redisTemplate.opsForValue().get("Long");//从redis取出 "1234567890987666"的byte[] 反序列化，实际类型是 Long:1234567890987666L


        redisTemplate.opsForValue().set("String", "中dd");//ObjectMapper将此String序列化成 "\"中dd\""的byte[], redis中保存为 "String"->"\"\xe4\xb8\xaddd\""
        Object stringObject = redisTemplate.opsForValue().get("String");//从redis取出 "\"中dd\""的byte[] 反序列化，实际类型是 String: "中dd"


        redisTemplate.opsForValue().set("Float", 1.32323232323232323232323232323254345656364564564564545f);//ObjectMapper将此Float序列化成 "1.3232323" 的byte[], redis中保存为 "Float"->"1.3232323" 精度丢失
        redisTemplate.opsForValue().set("Double", 2.332131231212445645674765756877867897853764645645645656546546456);//ObjectMapper将此Double序列化成 "2.3321312312124456"的byte[], redis中保存为 "Double"->"2.3321312312124456" 精度丢失
        Object floatObject = redisTemplate.opsForValue().get("Float");//从redis取出 "1.3232323"的byte[] 反序列化，实际类型是 Double
        Object doubleObject = redisTemplate.opsForValue().get("Double");//从redis取出 "2.3321312312124456" 的byte[] 反序列化，实际类型是 Double


        BigInteger bi = new BigInteger("771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121");
        BigDecimal bd = new BigDecimal("8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423");

        redisTemplate.opsForValue().set("BigInteger", bi);//ObjectMapper将此BigInteger序列化成 "[\"java.math.BigInteger\",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121]"的byte[]
        redisTemplate.opsForValue().set("BigDecimal", bd);//ObjectMapper将此BigDecimal序列化成 "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"的byte[]
        Object bigIntegerObject = redisTemplate.opsForValue().get("BigInteger");//从redis取出反序列化，实际类型是 BigInteger
        Object bigDecimalObject = redisTemplate.opsForValue().get("BigDecimal");//从redis取出反序列化，实际类型是 BigDecimal


        redisTemplate.opsForValue().set("Enum", PojoDtoEnum.SYSTEM.getValue());//Enum的value字段(String类型)序列化为 "\"SYSTEM\"" 的byte[], redis保存为 "Enum"->"\"SYSTEM\""
        Object enumStringObject = redisTemplate.opsForValue().get("Enum");//从redis取出反序列化，实际类型是 String: "SYSTEM"

        redisTemplate.opsForValue().set("LocalDateTime", LocalDateTime.now());//ObjectMapper将此LocalDateTime序列化成 "\"2021-05-17 18:03:44\"" 的byte[], redis保存为 "LocalDateTime"->"\"2021-05-17 18:03:44\""
        Object localDateTimeObject = redisTemplate.opsForValue().get("LocalDateTime");//从redis取出 "\"2021-05-17 18:03:44\""的byte[] 反序列化，实际类型是 String: "2021-05-17 18:03:44"

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("q中");
        redisTemplate.opsForValue().set("List", list);//ObjectMapper将此List序列化成 "["java.util.ArrayList",["1","q中"]]" 的byte[], redis保存为 "List"->"[\"java.util.ArrayList\",[\"1\",\"q\xe4\xb8\xad\"]]"
        Object listObject = redisTemplate.opsForValue().get("List");//从redis取出, 反序列化，实际类型是 ArrayList
        List<String> listGenericObject = (List<String>) listObject;//强转成List<String>


        //ObjectMapper对每一个list-value序列化，对Long序列化成 "4123123122"的byte[], 对String序列化成 "\"中\""的byte[], 对BigDecimal序列化成 "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"的byte[],
        //                                  对Enum的String序列化成 "\"SYSTEM\""的byte[], 对LocalDateTime序列化成 "\"2021-05-16 14:03:19\""的byte[]
        //redis中保存为
        //      127.0.0.1:6379> lrange list-value 0 5
        //      1) "\"2021-05-16 14:03:19\""
        //      2) "\"SYSTEM\""
        //      3) "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        //      4) "\"\xe4\xb8\xad\""
        //      5) "4123123122"
        redisTemplate.delete("list-value");
        redisTemplate.opsForList().leftPushAll("list-value", 4123123122L, "中", bd, PojoDtoEnum.SYSTEM.getValue(), LocalDateTime.now());
        Long length = redisTemplate.opsForList().size("list-value");
        //从redis取出反序列化，list中实际类型依次是 String,String,BigDecimal,String,Long
        List<Object> listValues = redisTemplate.opsForList().range("list-value", 0, length);


        //ObjectMapped对每一个hash-value序列化，对Enum的String序列化成 "\"SYSTEM\""的byte[], 对LocalDateTime序列化成 "\"2021-05-16 17:02:34\""的byte[], 对BigDecimal序列化成 "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"的byte[],
        //                                  对String序列化成 "\"中\""的byte[], 对Long序列化成 "124234252453453"的byte[]
        //redis中保存为
        //      127.0.0.1:6381> hgetall hash-value
        //       "key5"->"124234252453453",
        //       "key2"->"\"2021-05-16 17:02:34\"",
        //       "key3"->"[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]",
        //       "key4"->"\"\xe4\xb8\xad\"",
        //       "key1"->"\"SYSTEM\""
        redisTemplate.opsForHash().put("hash-value", "key1", PojoDtoEnum.SYSTEM.getValue());
        redisTemplate.opsForHash().put("hash-value", "key2", LocalDateTime.now());
        redisTemplate.opsForHash().put("hash-value", "key3", bd);
        redisTemplate.opsForHash().put("hash-value", "key4", "中");
        redisTemplate.opsForHash().put("hash-value", "key5", 124234252453453L);

        Object hashKey1 = redisTemplate.opsForHash().get("hash-value", "key1");//从redis取出反序列化，实际类型是String
        Object hashKey2 = redisTemplate.opsForHash().get("hash-value", "key2");//从redis取出反序列化，实际类型是String
        Object hashKey3 = redisTemplate.opsForHash().get("hash-value", "key3");//从redis取出反序列化，实际类型是BigDecimal
        Object hashKey4 = redisTemplate.opsForHash().get("hash-value", "key4");//从redis取出反序列化，实际类型是String
        Object hashKey5 = redisTemplate.opsForHash().get("hash-value", "key5");//从redis取出反序列化，实际类型是Long


        Pojo2RedisDto pojo2RedisDto = Pojo2RedisDto.builder()
                .id(1234534535354L)
                .bl(false)
                .s(null)
                .name("just pojo哒哒哒")
                .bi(bi)
                .bd(bd)
                .pojoType(PojoDtoEnum.SYSTEM.getValue())
                .pojoTime(LocalDateTime.now())
                .build();
        //ObjectMapper将此Pojo对象序列化成
        //"["com.sc.sample.redis.dto.Pojo2RedisDto",{"id":1234534535354,"bl":false,"s":null,"name":"just pojo哒哒哒","bi":["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],"bd":["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],"pojoType":"SYSTEM","pojoTime":"2021-05-17 18:06:34"}]"的byte[]
        //redis中保存为
        //"[\"com.sc.sample.redis.dto.Pojo2RedisDto\",{\"id\":1234534535354,\"bl\":false,\"s\":null,\"name\":\"just pojo\xe5\x93\x92\xe5\x93\x92\xe5\x93\x92\",\"bi\":[\"java.math.BigInteger\",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],\"bd\":[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],\"pojoType\":\"SYSTEM\",\"pojoTime\":\"2021-05-17 18:06:34\"}]"
        redisTemplate.opsForValue().set("Pojo", pojo2RedisDto);
        Object result = redisTemplate.opsForValue().get("Pojo");//result为Pojo2RedisDto类型
        Pojo2RedisDto resultToUse = (Pojo2RedisDto) result;

        
        List<Pojo2RedisDto> pojoList = new ArrayList<>();
        pojoList.add(pojo2RedisDto);
        //ObjectMapper将此List序列化成
        //"["java.util.ArrayList",[["com.sc.sample.redis.dto.Pojo2RedisDto",{"id":1234534535354,"bl":false,"s":null,"name":"just pojo哒哒哒","bi":["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],"bd":["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],"pojoType":"SYSTEM","pojoTime":"2021-05-29 10:48:50"}]]]"
        //redis中保存为
        //"[\"java.util.ArrayList\",[[\"com.sc.sample.redis.dto.Pojo2RedisDto\",{\"id\":1234534535354,\"bl\":false,\"s\":null,\"name\":\"just pojo\xe5\x93\x92\xe5\x93\x92\xe5\x93\x92\",\"bi\":[\"java.math.BigInteger\",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],\"bd\":[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],\"pojoType\":\"SYSTEM\",\"pojoTime\":\"2021-05-29 10:48:50\"}]]]"
        redisTemplate.opsForValue().set("PojoList", pojoList);
        Object listResult = redisTemplate.opsForValue().get("PojoList");
        List<Pojo2RedisDto> listToUse = (List<Pojo2RedisDto>) listResult;
        
        //the other 将bean写成redis 的 hash
        //1.将bean转化为Map<String, Object>
        Map<String, Object> pojoMap = ReflectUtils.bean2Map(resultToUse);
        //2.将Map<String, Object>写入redis hash, Map中每一个value都使用ObjectMapper序列化
        //127.0.0.1:6380> hgetall PojoHash
        // "pojoType"->"\"SYSTEM\""
        // "id"->"1234534535354"
        // "s"->""
        // "bi"->"[\"java.math.BigInteger\",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121]"
        // "bl"->"false"
        // "name"->"\"just pojo\xe5\x93\x92\xe5\x93\x92\xe5\x93\x92\""
        // "pojoTime"->"\"2021-05-18 17:27:42\""
        // "bd"->"[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        redisTemplate.opsForHash().putAll("PojoHash", pojoMap);
        //3.从redis取得Map，Map中每一个value都使用ObjectMapper反序列化
        Map<Object, Object> hashVal = redisTemplate.opsForHash().entries("PojoHash");
        //4.将Map转化为bean,  Map2Bean error: map中pojoTime是String，无法直接设置成bean的LocalDateTime对象, Pojo(有类型)-->Map(有类型)-->序列化保存到redis(有些字段类型丢失，如序列化成了String)-->从redis取出保存为Map(有些字段类型丢失)-->Pojo(报错: 有些字段类型不对)
        //Pojo2RedisDto bean = ReflectUtils.map2Bean(hashVal, Pojo2RedisDto.class);
        //link: 第3,4两步放在genericRedisTemplate


        return JsonResult.buildSuccessResult(resultToUse);
    }


    /**
     * 第三种操作方法为
     *   使用RedisTemplate<String, byte[]>,value,list-value,hash-value本身均是byte[],且均使用null: 如果序列化器为null并且已经是byte[]，就直接使用该byte[]:
     *   "将序列化和反序列化外放"（此为核心思想）:
     *   1.将对象序列化成byte[]: 使用ObjectMapper将任意对象序列化成byte[]
     *   2.使用genericRedisTemplate写到redis: 直接将byte[]发送给redis
     *   3.使用genericRedisTemplate从redis读,保存为byte[]: 从redis读出byte[],返回byte[]
     *   4.将byte[]反序列化成对象: 使用ObjectMapper将byte[]反序列化成想要的类型
     *
     * 这是stringRedisTemplate的替代方案
     *
     */
    @PostMapping("setBytes")
    public JsonResult setBytes() {

        Integer somethingNull = null;
        genericRedisTemplate.opsForValue().set("Null", flCustomSerializer.serializeAsBytes(somethingNull));//序列化为 byte[0], redis中保存为 "Null"->""
        byte[] nullBytes = genericRedisTemplate.opsForValue().get("Null");//从redis取出，保存为 byte[]
        Object nullVal1 = flCustomSerializer.deserialize(nullBytes); //反序列化为 null
        Integer nullVal2 = flCustomSerializer.deserialize(nullBytes, Integer.class);//反序列化为 null


        genericRedisTemplate.opsForValue().set("Boolean", flCustomSerializer.serializeAsBytes(true));//序列化为 byte[], redis中保存为 "Boolean"->"true"
        byte[] booleanBytes = genericRedisTemplate.opsForValue().get("Boolean");//从redis取出，保存为 byte[]
        Boolean booleanVal = flCustomSerializer.deserialize(booleanBytes, Boolean.class);//反序列化为 Boolean:true


        genericRedisTemplate.opsForValue().set("Character", flCustomSerializer.serializeAsBytes('a'));//序列化为 byte[], redis中保存为 "Character"->"\"a\""
        byte[] charBytes = genericRedisTemplate.opsForValue().get("Character");//从redis取出，保存为 byte[]
        Character charVal = flCustomSerializer.deserialize(charBytes, Character.class);//反序列化为 Character:'a'


        genericRedisTemplate.opsForValue().set("Byte", flCustomSerializer.serializeAsBytes((byte) 1));//序列化为 byte[], redis中保存为 "Byte"->"1"
        byte[] byteBytes = genericRedisTemplate.opsForValue().get("Byte");//从redis取出，保存为 byte[]
        Byte byteVal = flCustomSerializer.deserialize(byteBytes, Byte.class);//反序列化为 Byte:1


        genericRedisTemplate.opsForValue().set("Short", flCustomSerializer.serializeAsBytes((short) 2));//序列化为 byte[], redis中保存为 "Short"->"2"
        byte[] shortBytes = genericRedisTemplate.opsForValue().get("Short");  //从redis取出，保存为 byte[]
        Short shortVal = flCustomSerializer.deserialize(shortBytes, Short.class);//反序列化为 Short:2


        genericRedisTemplate.opsForValue().set("Integer", flCustomSerializer.serializeAsBytes(3));//序列化为 byte[], redis中保存为 "Integer"->"3"
        byte[] integerBytes = genericRedisTemplate.opsForValue().get("Integer");//从redis取出，保存为 byte[]
        Integer integerVal = flCustomSerializer.deserialize(integerBytes, Integer.class);//反序列化为 Integer:3


        genericRedisTemplate.opsForValue().set("Long", flCustomSerializer.serializeAsBytes(1234567890987666L));//序列化为 byte[], redis保存为 "Long"->"1234567890987666"
        byte[] longBytes = genericRedisTemplate.opsForValue().get("Long");//从redis取出，保存为byte[]
        Long longVal = flCustomSerializer.deserialize(longBytes, Long.class);//反序列化为 Long:1234567890987666L


        genericRedisTemplate.opsForValue().set("String", flCustomSerializer.serializeAsBytes("123中"));//序列化为 byte[], redis保存为 "String"->"\"123\xe4\xb8\xad\""
        byte[] stringBytes = genericRedisTemplate.opsForValue().get("String");//从redis取出，保存为 byte[]
        String stringVal = flCustomSerializer.deserialize(stringBytes, String.class);//反序列化为 String:"123中"


        genericRedisTemplate.opsForValue().set("Float", flCustomSerializer.serializeAsBytes(1.2345666666666666666675465645f));//序列化为 byte[], redis保存为 "Float"->"1.2345667"
        byte[] floatBytes = genericRedisTemplate.opsForValue().get("Float");//从redis取出，保存为 byte[]
        Float floatVal = flCustomSerializer.deserialize(floatBytes, Float.class);//反序列化为 Float


        genericRedisTemplate.opsForValue().set("Double", flCustomSerializer.serializeAsBytes(121.321312312312312323123123123131231));//序列化为 byte[], redis保存为 "Double"->"121.32131231231232"
        byte[] doubleBytes = genericRedisTemplate.opsForValue().get("Double");//从redis取出，保存为 byte[]
        Double doubleVal = flCustomSerializer.deserialize(doubleBytes, Double.class);//反序列化为 Double

        BigInteger bi = new BigInteger("771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121");
        BigDecimal bd = new BigDecimal("8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423");

        genericRedisTemplate.opsForValue().set("BigInteger", flCustomSerializer.serializeAsBytes(bi));//序列化为 byte[], redis保存为 "BigInteger"->"["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121]"
        byte[] bigIntegerBytes = genericRedisTemplate.opsForValue().get("BigInteger");//从redis取出，保存为 byte[]
        BigInteger biVal = flCustomSerializer.deserialize(bigIntegerBytes, BigInteger.class);//反序列化为BigInteger


        genericRedisTemplate.opsForValue().set("BigDecimal", flCustomSerializer.serializeAsBytes(bd));//序列化为 byte[], redis保存为 "BigDecimal"->"["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        byte[] bigDecimalBytes = genericRedisTemplate.opsForValue().get("BigDecimal");//从redis取出，保存为 byte[]
        BigDecimal bdVal = flCustomSerializer.deserialize(bigDecimalBytes, BigDecimal.class);//反序列化为BigDecimal


        genericRedisTemplate.opsForValue().set("Enum", flCustomSerializer.serializeAsBytes(PojoDtoEnum.SYSTEM.getValue()));//将Enum的value字段(String类型)序列化为 byte[], redis保存为 "Enum"->"\"SYSTEM\""
        byte[] enumBytes = genericRedisTemplate.opsForValue().get("Enum");//从redis取出，保存为 byte[]
        PojoDtoEnum enumVal = PojoDtoEnum.getByValue(flCustomSerializer.deserialize(enumBytes, String.class));//反序列化并使用该String构造Enum


        genericRedisTemplate.opsForValue().set("LocalDateTime", flCustomSerializer.serializeAsBytes(LocalDateTime.now()));//序列化为 byte[], redis保存为 "LocalDateTime"->"\"2021-05-16 22:05:41\""
        byte[] localDateTimeBytes = genericRedisTemplate.opsForValue().get("LocalDateTime");//从redis取出，保存为 byte[]
        LocalDateTime localDateTimeVal = flCustomSerializer.deserialize(localDateTimeBytes, LocalDateTime.class);//反序列为LocalDateTime


        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("q中");
        genericRedisTemplate.opsForValue().set("List", flCustomSerializer.serializeAsBytes(list));//序列化为 byte[], redis保存为 "List"->"[\"java.util.ArrayList\",[\"1\",\"q\xe4\xb8\xad\"]]"
        byte[] listBytes = genericRedisTemplate.opsForValue().get("List");//从redis取出, 保存为 byte[]
        List<String> listVal = flCustomSerializer.deserialize(listBytes, new TypeReference<List<String>>(){}); //反序列化为List<String>


        //对每一个list-value序列化为 byte[]
        //redis中保存为
        //      127.0.0.1:6379> lrange list-value 0 5
        //      1) "\"2021-05-16 14:03:19\""
        //      2) "\"SYSTEM\""
        //      3) "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        //      4) "\"\xe4\xb8\xad\""
        //      5) "4123123122"
        genericRedisTemplate.delete("list-value");
        genericRedisTemplate.opsForList().leftPushAll("list-value", flCustomSerializer.serializeAsBytes(4123123122L),
                flCustomSerializer.serializeAsBytes("中"), flCustomSerializer.serializeAsBytes(bd), flCustomSerializer.serializeAsBytes(PojoDtoEnum.SYSTEM.getValue()),
                flCustomSerializer.serializeAsBytes(LocalDateTime.now()));
        Long length = genericRedisTemplate.opsForList().size("list-value");
        //从redis取出，保存为List<String>: "\"2021-05-16 14:03:19\"", "\"SYSTEM\"", "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]", "\"中\"", "4123123122"
        List<byte[]> listValues = genericRedisTemplate.opsForList().range("list-value", 0, length);
        //对每一个值反序列化...


        //对每一个hash-value序列化为 byte[]
        //redis中保存为
        //      127.0.0.1:6381> hgetall hash-value
        //       "key5"->"124234252453453",
        //       "key2"->"\"2021-05-16 17:02:34\"",
        //       "key3"->"[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]",
        //       "key4"->"\"\xe4\xb8\xad\"",
        //       "key1"->"\"SYSTEM\""
        genericRedisTemplate.opsForHash().put("hash-value", "key1", flCustomSerializer.serializeAsBytes(PojoDtoEnum.SYSTEM.getValue()));
        genericRedisTemplate.opsForHash().put("hash-value", "key2", flCustomSerializer.serializeAsBytes(LocalDateTime.now()));
        genericRedisTemplate.opsForHash().put("hash-value", "key3", flCustomSerializer.serializeAsBytes(bd));
        genericRedisTemplate.opsForHash().put("hash-value", "key4", flCustomSerializer.serializeAsBytes("中"));
        genericRedisTemplate.opsForHash().put("hash-value", "key5", flCustomSerializer.serializeAsBytes(124234252453453L));

        byte[] hashKey1 = (byte[]) genericRedisTemplate.opsForHash().get("hash-value", "key1");//从redis取出，保存为 byte[]
        String hashKey1Val = flCustomSerializer.deserialize(hashKey1, String.class);//反序列化为String: "SYSTEM"

        byte[] hashKey2 = (byte[]) genericRedisTemplate.opsForHash().get("hash-value", "key2");//从redis取出，保存为 byte[]
        LocalDateTime hashKey2Val = flCustomSerializer.deserialize(hashKey2, LocalDateTime.class);//反序列化为LocalDateTime

        byte[] hashKey3 = (byte[]) genericRedisTemplate.opsForHash().get("hash-value", "key3");//从redis取出，保存为 byte[]
        BigDecimal hashKey3Val = flCustomSerializer.deserialize(hashKey3, BigDecimal.class);//反序列化为BigDecimal

        byte[] hashKey4 = (byte[]) genericRedisTemplate.opsForHash().get("hash-value", "key4");//从redis取出，保存为 byte[]
        String hashKey4Val = flCustomSerializer.deserialize(hashKey4, String.class);//反序列化为 String: "中"

        byte[] hashKey5 = (byte[]) genericRedisTemplate.opsForHash().get("hash-value", "key5");//从redis取出，保存为 byte[]
        Long hashKey5Val = flCustomSerializer.deserialize(hashKey5, Long.class);//反序列化为 Long


        Pojo2RedisDto pojo2RedisDto = Pojo2RedisDto.builder()
                .id(1234534535354L)
                .bl(false)
                .s(null)
                .name("just pojo哒哒哒")
                .bi(bi)
                .bd(bd)
                .pojoType(PojoDtoEnum.SYSTEM.getValue())
                .pojoTime(LocalDateTime.now())
                .build();
        //1.序列化Pojo为 byte[]
        byte[] se = flCustomSerializer.serializeAsBytes(pojo2RedisDto);
        //2.保存到redis
        //"[\"com.sc.sample.redis.dto.Pojo2RedisDto\",{\"id\":1234534535354,\"bl\":false,\"s\":null,\"name\":\"just pojo\xe5\x93\x92\xe5\x93\x92\xe5\x93\x92\",\"bi\":[\"java.math.BigInteger\",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],\"bd\":[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],\"pojoType\":\"SYSTEM\",\"pojoTime\":\"2021-05-17 22:30:39\"}]"
        genericRedisTemplate.opsForValue().set("Pojo", se);
        //3.从redis取出，保存为byte[]
        byte[] result = genericRedisTemplate.opsForValue().get("Pojo");
        //4.反序列化为Pojo
        Pojo2RedisDto pojoDe = flCustomSerializer.deserialize(result, Pojo2RedisDto.class);

        //the other 将bean写成redis 的 hash
        //3.从redis取得Map，Map中value是byte[]
        Map<Object, Object> hashVal = genericRedisTemplate.opsForHash().entries("PojoHash");
        //4.将Map转化为bean
        Pojo2RedisDto bean = ReflectUtils.map2Bean(hashVal, Pojo2RedisDto.class, flCustomSerializer);

        return JsonResult.buildSuccessResult(pojoDe);
    }


    @GetMapping("test1")
    public JsonResult test1() {
        //先使用setString调用写入，在用RedisTemplate读取
        Object booleanObject = redisTemplate.opsForValue().get("Boolean");//从redis取出 "true" 反序列化，实际类型是 Boolean:true
        Object charObject = redisTemplate.opsForValue().get("Character");//从redis取出 "\"a\"" 反序列化，实际类型是 String:"a"
        Object byteObject = redisTemplate.opsForValue().get("Byte");//从redis取出 "1" 反序列化，实际类型是 Integer:1
        Object shortObject = redisTemplate.opsForValue().get("Short");//从redis取出 "2" 反序列化，实际类型是 Integer: 2
        Object integerObject = redisTemplate.opsForValue().get("Integer");//从redis取出 "3" 反序列化，实际类型是 Integer:3
        Object longObject = redisTemplate.opsForValue().get("Long");//从redis取出 "1234567890987666" 反序列化，实际类型是 Long:1234567890987666L
        Object stringObject = redisTemplate.opsForValue().get("String");//从redis取出 "\"中dd\"" 反序列化，实际类型是 String: "中dd"
        Object floatObject = redisTemplate.opsForValue().get("Float");//从redis取出 "1.3232323" 反序列化，实际类型是 Double
        Object doubleObject = redisTemplate.opsForValue().get("Double");//从redis取出 "2.3321312312124456" 反序列化，实际类型是 Double
        Object bigIntegerObject = redisTemplate.opsForValue().get("BigInteger");//从redis取出反序列化，实际类型是 BigInteger
        Object bigDecimalObject = redisTemplate.opsForValue().get("BigDecimal");//从redis取出反序列化，实际类型是 BigDecimal
        Object enumStringObject = redisTemplate.opsForValue().get("Enum");//从redis取出反序列化，实际类型是 String: "SYSTEM"
        Object localDateTimeObject = redisTemplate.opsForValue().get("LocalDateTime");//从redis取出 "\"2021-05-17 18:03:44\"" 反序列化，实际类型是 String: "2021-05-17 18:03:44"

        Long length = redisTemplate.opsForList().size("list-value");
        //从redis取出反序列化，list中实际类型依次是 String,String,BigDecimal,String,Long
        List<Object> listValues = redisTemplate.opsForList().range("list-value", 0, length);

        Object hashKey1 = redisTemplate.opsForHash().get("hash-value", "key1");//从redis取出反序列化，实际类型是String
        Object hashKey2 = redisTemplate.opsForHash().get("hash-value", "key2");//从redis取出反序列化，实际类型是String
        Object hashKey3 = redisTemplate.opsForHash().get("hash-value", "key3");//从redis取出反序列化，实际类型是BigDecimal
        Object hashKey4 = redisTemplate.opsForHash().get("hash-value", "key4");//从redis取出反序列化，实际类型是String
        Object hashKey5 = redisTemplate.opsForHash().get("hash-value", "key5");//从redis取出反序列化，实际类型是Long

        Object result = redisTemplate.opsForValue().get("Pojo");
        Pojo2RedisDto resultToUse = (Pojo2RedisDto) result;


        return JsonResult.buildSuccessResult("测试成功");
    }


    @GetMapping("test2")
    public JsonResult test2() {
        //在使用setObject调用写入，在用StringRedisTemplate读取
        String booleanString = customRedisTemplate.opsForValue().get("Boolean");   //从redis取出，保存为 "true"
        Boolean booleanVal = flCustomSerializer.deserialize(booleanString, Boolean.class);//反序列化为 Boolean:true

        String charString = customRedisTemplate.opsForValue().get("Character");    //从redis取出，保存为 "\"a\""
        Character charVal = flCustomSerializer.deserialize(charString, Character.class);//反序列化为 Character:'a'

        String byteString = customRedisTemplate.opsForValue().get("Byte");    //从redis取出，保存为 "1"
        Byte byteVal = flCustomSerializer.deserialize(byteString, Byte.class);//反序列化为 Byte:1

        String shortString = customRedisTemplate.opsForValue().get("Short");  //从redis取出，保存为 "2"
        Short shortVal = flCustomSerializer.deserialize(shortString, Short.class);//反序列化为 Short:2

        String integerString = customRedisTemplate.opsForValue().get("Integer");//从redis取出，保存为 "3"
        Integer integerVal = flCustomSerializer.deserialize(integerString, Integer.class);//反序列化为 Integer:3

        String longString = customRedisTemplate.opsForValue().get("Long");//从redis取出，保存为"1234567890987666"
        Long longVal = flCustomSerializer.deserialize(longString, Long.class);//反序列化为 Long:1234567890987666L

        String stringString = customRedisTemplate.opsForValue().get("String");//从redis取出，保存为 "\"123中\""
        String stringVal = flCustomSerializer.deserialize(stringString, String.class);//反序列化为 String:"123中"

        String floatString = customRedisTemplate.opsForValue().get("Float");//从redis取出，保存为 "1.2345667"
        Float floatVal = flCustomSerializer.deserialize(floatString, Float.class);//反序列化为 Float

        String doubleString = customRedisTemplate.opsForValue().get("Double");//从redis取出，保存为 "121.32131231231232"
        Double doubleVal = flCustomSerializer.deserialize(doubleString, Double.class);//反序列化为 Double

        String bigIntegerString = customRedisTemplate.opsForValue().get("BigInteger");//从redis取出，保存为 "["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121]"
        BigInteger biVal = flCustomSerializer.deserialize(bigIntegerString, BigInteger.class);//反序列化为BigInteger

        String bigDecimalString = customRedisTemplate.opsForValue().get("BigDecimal");//从redis取出，保存为 "["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        BigDecimal bdVal = flCustomSerializer.deserialize(bigDecimalString, BigDecimal.class);//反序列化为BigDecimal

        String enumString = customRedisTemplate.opsForValue().get("Enum");//从redis取出，保存为 "\"SYSTEM\""
        PojoDtoEnum enumVal = PojoDtoEnum.getByValue(flCustomSerializer.deserialize(enumString, String.class));//反序列化并使用该String构造Enum

        String localDateTimeString = customRedisTemplate.opsForValue().get("LocalDateTime");//从redis取出，保存为 "\"2021-05-16 22:05:41\""
        LocalDateTime localDateTimeVal = flCustomSerializer.deserialize(localDateTimeString, LocalDateTime.class);//反序列为LocalDateTime

        Long length = customRedisTemplate.opsForList().size("list-value");
        //从redis取出，保存为List<String>: "\"2021-05-16 14:03:19\"", "\"SYSTEM\"", "[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]", "\"中\"", "4123123122"
        List<String> listValues2 = customRedisTemplate.opsForList().range("list-value", 0, length);
        //对每一个值反序列化...


        String hashKey1 = (String) customRedisTemplate.opsForHash().get("hash-value", "key1");//从redis取出，保存为"\"SYSTEM\""
        String hashKey1Val = flCustomSerializer.deserialize(hashKey1, String.class);//反序列化为String: "SYSTEM"

        String hashKey2 = (String) customRedisTemplate.opsForHash().get("hash-value", "key2");//从redis取出，保存为"\"2021-05-16 17:02:34\""
        LocalDateTime hashKey2Val = flCustomSerializer.deserialize(hashKey2, LocalDateTime.class);//反序列化为LocalDateTime

        String hashKey3 = (String) customRedisTemplate.opsForHash().get("hash-value", "key3");//从redis取出，保存为"[\"java.math.BigDecimal\",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
        BigDecimal hashKey3Val = flCustomSerializer.deserialize(hashKey3, BigDecimal.class);//反序列化为BigDecimal

        String hashKey4 = (String) customRedisTemplate.opsForHash().get("hash-value", "key4");//从redis取出，保存为"\"中\""
        String hashKey4Val = flCustomSerializer.deserialize(hashKey4, String.class);//反序列化为 String: "中"

        String hashKey5 = (String) customRedisTemplate.opsForHash().get("hash-value", "key5");//从redis取出，保存为"124234252453453"
        Long hashKey5Val = flCustomSerializer.deserialize(hashKey5, Long.class);//反序列化为 Long


        //3.从redis取出，保存为String
        //"["com.sc.sample.redis.dto.Pojo2RedisDto",{"id":1234534535354,"bl":false,"s":null,"name":"just pojo哒哒哒","bi":["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],"bd":["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],"pojoType":"SYSTEM","pojoTime":"2021-05-17 22:30:39"}]"
        String result = customRedisTemplate.opsForValue().get("Pojo");
        //4.反序列化为Pojo
        Pojo2RedisDto pojoDe = flCustomSerializer.deserialize(result, Pojo2RedisDto.class);

        return JsonResult.buildSuccessResult("测试成功");
    }


    @PostMapping("del")
    public JsonResult del(@RequestBody @Valid SampleRedisTestDelDto redisTestDelDto) {
        Boolean result = redisTemplate.delete(redisTestDelDto.getKey());
        if(result) return JsonResult.buildSuccessResult("删除成功,key="+redisTestDelDto.getKey());
        else return JsonResult.buildFailedResult("删除失败,key="+redisTestDelDto.getKey());
    }



    //using numberRedisTemplate start
    @Deprecated
    @PostMapping("setNumber")
    public JsonResult setNumber() {
        //key-value,value是Number类型
        numberRedisTemplate.opsForValue().set("Byte", (byte) 1);  //127.0.0.1:6379> get Byte 返回 "1";         NumberSerializers$IntLikeSerializer
        numberRedisTemplate.opsForValue().set("Short", 2);        //127.0.0.1:6379> get Short 返回 "2";        NumberSerializers$IntegerSerializer
        numberRedisTemplate.opsForValue().set("Integer", 3);      //127.0.0.1:6379> get Integer 返回 "3";      NumberSerializers$IntegerSerializer
        numberRedisTemplate.opsForValue().set("Long", 4L);        //127.0.0.1:6379> get Long 返回 "4";         NumberSerializers$LongSerializer
        numberRedisTemplate.opsForValue().set("Float", 5.5f);     //127.0.0.1:6379> get Float 返回 "5.5";      NumberSerializers$FloatSerializer
        numberRedisTemplate.opsForValue().set("Double", 6.6);     //127.0.0.1:6379> get Double 返回 "6.6";     NumberSerializers$DoubleSerializer
        numberRedisTemplate.opsForValue().set("BigInteger", new BigInteger("775453453453455345345345345345345534534534534535453453453453535356267567765426245646464645654646456245624562"));  //127.0.0.1:6379> get BigInteger 返回 "77";   NumberSerializer
        numberRedisTemplate.opsForValue().set("BigDecimal", new BigDecimal("8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423")); //127.0.0.1:6379> get BigDecimal 返回 "8.999901";   NumberSerializer

        return JsonResult.buildSuccessResult("保存成功");
    }

    @Deprecated
    @GetMapping("getNumber")
    public JsonResult getNumber() {
        //key-value,value是Number类型
        Number byteNumber = numberRedisTemplate.opsForValue().get("Byte");  //all NumberDeserializers$NumberDeserializer
        Number shortNumber = numberRedisTemplate.opsForValue().get("Short");   //取出来的值是2，被当作Integer
        Number integerNumber = numberRedisTemplate.opsForValue().get("Integer");
        Number longNumber = numberRedisTemplate.opsForValue().get("Long");  //取出来的值是4，被当作Integer
        Number floatNumber = numberRedisTemplate.opsForValue().get("Float"); //取出来的值是5.5，被当作Double
        Number doubleNumber = numberRedisTemplate.opsForValue().get("Double"); //取出来的值是6.6，被当作Double
        Number bigIntegerNumber = numberRedisTemplate.opsForValue().get("BigInteger");  //取出来的值是77，被当作Integer  ;大数字能够被正确解析成BigInteger
        Number bigDecimalNumber = numberRedisTemplate.opsForValue().get("BigDecimal"); //被当作double，存在精度问题


        return JsonResult.buildSuccessResult("number");
    }
    //using numberRedisTemplate end


}
