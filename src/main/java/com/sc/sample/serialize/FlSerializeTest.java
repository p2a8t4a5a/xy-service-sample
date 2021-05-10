package com.sc.sample.serialize;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.sc.common.enums.ScanTypeEnum;
import com.sc.sample.enums.PojoAnoEnum;
import com.sc.sample.enums.PojoEnum;
import com.sc.sample.vo.scan.SampleScanVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FlSerializeTest {

    public static void main(String[] argv) throws Exception {

      /*
      * RedisConfig中序列化器试验
      */
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        om.registerModule(javaTimeModule);


        byte[] digitResult = om.writeValueAsBytes(1);  //49    对比digitResult2Bytes，即可以理解成 序列化成String后在某一字符集下的bytes形式
        String digitResult2 = om.writeValueAsString(1); //数字1 序列化成String: "1"
        byte[] digitResult2Bytes = digitResult2.getBytes("UTF-8"); //"1"在UTF8中的字符编码: 49

        byte[] charResult = om.writeValueAsBytes('a'); //34 97 34  对比charResult2Bytes，即可以理解成 序列化成String后在某一字符集下的bytes形式
        String charResult2 = om.writeValueAsString('a');//字符a 序列化成String: ""a""，注意里面有"
        byte[] charResult2Bytes = charResult2.getBytes("UTF-8"); //""a""在UTF8中编码: 34 97 34，其中34表示", 97表示a

        byte[] strResult = om.writeValueAsBytes("cluster 集群");  //34,99,108,117,115,116,101,114,32,-23 -101 -122 -25 -66,-92,34  与上述一致
        String strResult2 = om.writeValueAsString("cluster 集群");//"cluster 集群" 序列化成String: ""cluster 集群""，注意里面有"  ,  note: 所以之前混用StringRedisTemplate,RedisTemplate<String,Object>的错误即在于此: 两者序列化字符串使用的不同序列化器
        byte[] strResult2Bytes = strResult2.getBytes("UTF-8"); //34,99,108,117,115,116,101,114,32,-23 -101 -122 -25 -66,-92,34
                                                                          // ", c,  l,  u,  s,  t,  e,  r,空格,   集,           群,       "

        LocalDateTime date = LocalDateTime.now();
        byte[] dateResult = om.writeValueAsBytes(date);     //与上述一致
        String dateResult2 = om.writeValueAsString(date);  //序列化成 {"date":{"year":2021,"month":"MAY","day":10,"era":["java.time.chrono.IsoEra","CE"],"dayOfMonth":10,"dayOfWeek":"MONDAY","dayOfYear":130,"leapYear":false,"monthValue":5,"chronology":{"id":"ISO","calendarType":"iso8601"},"prolepticMonth":24256},"time":{"hour":14,"minute":57,"second":42,"nano":586000000},"dayOfMonth":10,"dayOfWeek":"MONDAY","dayOfYear":130,"month":"MAY","monthValue":5,"year":2021,"hour":14,"minute":57,"nano":586000000,"second":42,"chronology":["java.time.chrono.IsoChronology",{"id":"ISO","calendarType":"iso8601"}]}

        byte[] enumResult = om.writeValueAsBytes(PojoEnum.SYSTEM);  //与上述一致
        String enumResult2 = om.writeValueAsString(PojoEnum.SYSTEM);  //序列化成了 ""SYSTEM""

        Pojo pojo = Pojo.builder().id(1L).name("someiii阿迪斯").pojoEnum(PojoEnum.SYSTEM).pojoTime(date).build();
        byte[] pojoResult = om.writeValueAsBytes(pojo);  //与上述一致
        String pojoResult2 = om.writeValueAsString(pojo); //["com.sc.sample.serialize.Pojo",{"id":1,"name":"someiii阿迪斯","pojoEnum":"SYSTEM","pojoTime":{"date":{"year":2021,"month":"MAY","day":10,"era":["java.time.chrono.IsoEra","CE"],"dayOfMonth":10,"dayOfWeek":"MONDAY","dayOfYear":130,"leapYear":false,"monthValue":5,"chronology":{"id":"ISO","calendarType":"iso8601"},"prolepticMonth":24256},"time":{"hour":15,"minute":29,"second":37,"nano":747000000},"dayOfMonth":10,"dayOfWeek":"MONDAY","dayOfYear":130,"month":"MAY","monthValue":5,"year":2021,"hour":15,"minute":29,"nano":747000000,"second":37,"chronology":["java.time.chrono.IsoChronology",{"id":"ISO","calendarType":"iso8601"}]}}]


        Date dt = new Date(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        //使用jackson注解控制Pojo序列化格式
        PojoAno pojoAno = PojoAno.builder().id(1L).name("someiii阿迪斯").pojoEnumValue(PojoAnoEnum.SYSTEM.getValue()).dt(dt).pojoTime(date).build();
        byte[] pojoAnoResult = om.writeValueAsBytes(pojoAno);  //与上述一致
        //date+@JsonFormat有效; LocalDateTime+@JsonFormat默认无效,需要注册LocalDateTime的序列化器
        String pojoAnoResult2 = om.writeValueAsString(pojoAno); //date
        PojoAno r2 = om.readValue(pojoAnoResult2, PojoAno.class);


        String r = om.writeValueAsString(SampleScanVo.builder().id(1L).name("qd的").scanType(ScanTypeEnum.SYSTEM).scanTime(LocalDateTime.now()).build());
        SampleScanVo pojoAnoSe = om.readValue(r, SampleScanVo.class);
        Object objSe = om.readValue(r, Object.class);


        System.out.println("omb");
    }


}
