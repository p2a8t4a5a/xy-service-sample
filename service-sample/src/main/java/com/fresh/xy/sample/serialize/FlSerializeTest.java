package com.fresh.xy.sample.serialize;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fresh.common.utils.JacksonUtils;
import com.fresh.common.utils.ReflectUtils;
import com.fresh.xy.redis.enums.ForRedisTestPojoAnoEnum;
import com.fresh.xy.redis.enums.ForRedisTestPojoEnum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FlSerializeTest {

    public static void theBasicUse() throws Exception {

        ObjectMapper om = new ObjectMapper();
        om.registerModule(JacksonUtils.defaultJavaTimeModule());

        //序列化 与 反序列化
        /**
         * NullSerializer序列化器， null--->"null"
         */
        String nullSe = om.writeValueAsString(null);
        //"null"--->null
        Object nullObj = om.readValue(nullSe, Object.class);

        /**
         * Boolean类型，使用BooleanSerializer序列化器，true-->"true"; false--->"false"
         */
        String booleanSe = om.writeValueAsString(true);
        //UntypedObjectDeserializer，booleanObj的实际类型是Boolean
        Object booleanObj = om.readValue(booleanSe, Object.class);
        /**
         * NumberDeserializers$BooleanDeserializer反序列化器，"true"--->true; "false"--->false
         */
        Boolean booleanVal = om.readValue(booleanSe, Boolean.class);

        /**
         * Character类型，使用ToStringSerializer序列化器，'a'.toString()--->"\"a\""
         */
        String charSe = om.writeValueAsString('a');
        //UntypedObjectDeserializer，charObj实际类型为String
        Object charObj = om.readValue(charSe, Object.class);
        /**
         * NumberDeserializers$CharacterDeserializer反序列化器，"\"a\""--->'a'
         */
        Character charVal = om.readValue(charSe, Character.class);
        
        /**
         * Byte类型，使用NumberSerializers$IntLikeSerializer序列化器，(Number)var.intValue(),...--->"1"
         */
        String byteSe = om.writeValueAsString((byte) 1);
        //UntypedObjectDeserializer，byteObj实际类型为Integer
        Object byteObj = om.readValue(byteSe, Object.class);
        /**
         * NumberDeserializers$ByteDeserializer序列化器，"1"--->Byte
         */
        Byte byteVal = om.readValue(byteSe, Byte.class);
        

        /**
         * Short类型，使用NumberSerializers$ShortSerializer序列化器，(Short)var.shortValue(),...--->"2"
         */
        String shortSe = om.writeValueAsString((short) 2);
        //UntypedObjectDeserializer，shortObj实际类型为Integer
        Object shortObj = om.readValue(shortSe, Object.class);
        /**
         * NumberDeserializers$ShortDeserializer序列化器，"2"--->Short
         */
        Short shortVal = om.readValue(shortSe, Short.class);


        /**
         * Integer类型，使用NumberSerializers$IntegerSerializer序列化器，(Integer)var.intValue(),...--->"3"
         */
        String integerSe = om.writeValueAsString(3);
        //UntypedObjectDeserializer，integerObj实际类型为Integer
        Object integerObj = om.readValue(integerSe, Object.class);
        /**
         * NumberDeserializers$IntegerDeserializer, "3"--->Integer
         */
        Integer integerVal = om.readValue(integerSe, Integer.class);
        
        
        /**
         * Long类型，使用NumberSerializers$LongSerializer序列化器，(Long)var.longValue(),...--->"123456789098777777"
         */
        String longSe = om.writeValueAsString(123456789098777777L);
        //UntypedObjectDeserializer，longObj实际类型为Long
        Object longObj = om.readValue(longSe, Object.class);
        /**
         * NumberDeserializers$LongDeserializer, "123456789098777777"--->Long
         */
        Long longVal = om.readValue(longSe, Long.class);


        /**
         * String类型，使用StringSerializer序列化器，"\"中daddsdsa\""
         */
        String stringSe = om.writeValueAsString("中daddsdsa");
        //UntypedObjectDeserializer，stringObj实际类型为String
        Object stringObj = om.readValue(stringSe, Object.class);
        /**
         * StringDeserializer反序列化器，"中daddsdsa"
         */
        String stringVal = om.readValue(stringSe, String.class);


        /**
         * Float类型，使用NumberSerializers$FloatSerializer序列化器，(Float)var.floatValue(),...--->"1.2345667" 精度丢失
         */
        String floatSe = om.writeValueAsString(1.2345666666666666666675465645f);
        //UntypedObjectDeserializer，floatObj实际类型为Double，丢失精度后的值
        Object floatObj = om.readValue(floatSe, Object.class);
        /**
         * NumberDeserializers$FloatDeserializer, 丢失精度后的值
         */
        Float floatVal = om.readValue(floatSe, Float.class);


        /**
         * Double类型，使用NumberSerializers$DoubleSerializer序列化器，(Double)var.doubleValue(),...--->"121.32131231231232"  精度丢失
         */
        String doubleSe = om.writeValueAsString(121.321312312312312323123123123131231);
        //UntypedObjectDeserializer，doubleObj实际类型为Double，丢失精度后的值
        Object doubleObj = om.readValue(doubleSe, Object.class);
        /**
         * NumberDeserializers$DoubleDeserializer, 丢失精度后的值
         */
        Double doubleVal = om.readValue(doubleSe, Double.class);


        BigInteger bi = new BigInteger("771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121");
        BigDecimal bd = new BigDecimal("8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423");
        /**
         * BigInteger类型，使用NumberSerializer序列化器，bigInteger.toString()--->"771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121"
         */
        String biSe = om.writeValueAsString(bi);
        //UntypedObjectDeserializer，biObj实际类型为BigInteger
        Object biObj = om.readValue(biSe, Object.class);
        /**
         * NumberDeserializers$BigIntegerDeserializer
         */
        BigInteger biVal = om.readValue(biSe, BigInteger.class);
        /**
         * BigDecimal类型，使用NumberSerializer序列化器，bigDecimal.toString()--->"8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423"
         */
        String bdSe = om.writeValueAsString(bd);
        //UntypedObjectDeserializer，bdObj实际类型为Double，丢失精度，坑
        Object bdObj = om.readValue(bdSe, Object.class);
        /**
         * NumberDeserializers$BigDecimalDeserializer
         */
        BigDecimal bdVal = om.readValue(bdSe, BigDecimal.class);


        LocalDateTime date = LocalDateTime.now();
        /**
         * LocalDateTime类型，使用LocalDateTimeSerializer序列化器(需要注册module)，根据formatter格式化成日期字符串--->"\"2021-05-16 22:05:41\""
         */
        String dateSe = om.writeValueAsString(date);
        //UntypedObjectDeserializer，dateObj实际类型为String，"2021-05-16 22:05:41"
        Object dateObj = om.readValue(dateSe, Object.class);
        /**
         * LocalDateTimeDeserializer，将"2021-05-16 22:05:41"反序列化为LocalDateTime(使用对应的DateTimeFormatter)
         */
        LocalDateTime dateVal = om.readValue(dateSe, LocalDateTime.class);


        /**
         * Enum类型(没有加@JsonFormat,@JsonCreator)，使用EnumSerializer序列化器，PojoEnum.SYSTEM--->"\"SYSTEM\""
         */
        String pojoEnumSe = om.writeValueAsString(ForRedisTestPojoEnum.SYSTEM);
        //UntypedObjectDeserializer，enumObj实际类型为String, "SYSTEM"
        Object pojoEnumObj = om.readValue(pojoEnumSe, Object.class);
        /**
         * EnumDeserializer反序列化器，"SYSTEM"--->PojoEnum.SYSTEM
         */
        ForRedisTestPojoEnum pojoEnumVal = om.readValue(pojoEnumSe, ForRedisTestPojoEnum.class);

        /**
         * Enum with @JsonFormat、@JsonCreator，使用BeanSerializer序列化器
         * "{"value":"SYSTEM","text":"系统"}"
         */
        String pojoAnoEnumSe = om.writeValueAsString(ForRedisTestPojoAnoEnum.SYSTEM);
        //UntypedObjectDeserializer，enumObj实际类型为LinkedHashMap
        Object pojoAnoEnumObj = om.readValue(pojoAnoEnumSe, Object.class);
        /**
         * FactoryBasedEnumDeserializer反序列化器
         * "{"value":"SYSTEM","text":"系统"}"这个字符串没有对应的Enum实例，返回null
         */
        ForRedisTestPojoAnoEnum pojoAnoEnumVal = om.readValue(pojoAnoEnumSe, ForRedisTestPojoAnoEnum.class);


        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("q中");
        /**
         * IndexedListSerializer,StringSerializer序列化器
         * "["1","q中"]"
         */
        String listSe = om.writeValueAsString(list);
        //UntypedObjectDeserializer, ArrayList<Object>
        Object listObj = om.readValue(listSe, Object.class);
        //CollectionDeserializer,UnTypedObjectDeserializer(List中元素)序列化器， ArrayList<Object>
        List listVal = om.readValue(listSe, List.class);
        /**
         * 泛型类型，StringCollectionDeserializer, ArrayList<String>
         */
        List<String> listGenericVal = om.readValue(listSe, new TypeReference<List<String>>(){});


        Date dt = new Date(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        //Date未使用@JsonFormat,Enum未使用@JsonFormat,@JsonCreator
        ForSerializeTestPojo pojo = ForSerializeTestPojo.builder()
                .id(1L)              //NumberSerializers$LongSerializer序列化器     NumberDeserializers$LongDeserializer反序列化器
                .bl(true)            //BooleanSerializer序列化器                    NumberDeserializers$BooleanDeserializer反序列化器
                .name("someiii阿迪斯")  //StringSerializer序列化器                    StringDeserializer反序列化器
                .pojoEnum(ForRedisTestPojoEnum.SYSTEM)  //EnumSerializer序列化器     EnumDeserializer反序列化器
                .pojoTime(date)          //使用注册的LocalDateTimeSerializer序列化器   LocalDateTimeDeserializer反序列化器
                .bi(bi)                //使用NumberSerializer序列化器                 NumberDeserializers$BigIntegerDeserializer反序列化器
                .bd(bd)              //使用NumberSerializer序列化器                   NumberDeserializers$BigIntegerDeserializer反序列化器
                .dt(dt)              //DateSerializer序列化器                        DateDeserializers$DateDeserializer反序列化器
                .build();
        /**
         * BeanSerializer序列化器
         * "{"bl":true,"id":1,"name":"someiii阿迪斯","pojoEnum":"SYSTEM","pojoTime":"2021-05-17 00:56:24","bi":771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121,"bd":8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423,"dt":1621174581162}"
         */
        String pojoSe = om.writeValueAsString(pojo);
        //UntypedObjectDeserializer，pojo,pojo中每一个字段都使用此反序列化器，pojoObj实际类型为LinkedHashMap
        Object pojoObj = om.readValue(pojoSe, Object.class);
        /**
         * BeanDeserializer反序列化器，Pojo对象
         * Pojo(bl=true, id=1, name=someiii阿迪斯, pojoEnum=SYSTEM, pojoTime=2021-05-17T00:56:24, bi=771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121, bd=8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423, dt=Mon May 17 00:56:24 CST 2021)
         */
        ForSerializeTestPojo pojoVal = om.readValue(pojoSe, ForSerializeTestPojo.class);


        //Date使用@JsonFromat，Enum使用@JsonFormat，@JsonCreator
        ForSerializeTestPojoAno pojoAno = ForSerializeTestPojoAno.builder()
                .id(1L)                             //NumberSerializers$LongSerializer序列化器          NumberDeserializers$LongDeserializer反序列化器
                .bl(false)                          //BooleanSerializer序列化器                         NumberDeserializers$BooleanDeserializer反序列化器
                .name("someiii阿迪斯")               //StringSerializer序列化器                          StringDeserializer反序列化器
                .pojoAnoEnum(ForRedisTestPojoAnoEnum.SYSTEM.getValue()) //StringSerializer序列化器      StringDeserializer反序列化器
                .pojoTime(date)                     //注册的LocalDateTimeSerializer序列化器              LocalDateTimeDeserializer反序列化器
                .bi(bi)                             //NumberSerializer序列化器                          NumberDeserializers$BigIntegerDeserializer反序列化器
                .bd(bd)                             //NumberSerializer序列化器                          NumberDeserializers$BigIntegerDeserializer反序列化器
                .dt(dt)                             //DateSerializer，按@JsonFormat注解序列化            DateDeserializers$DateDeserializer反序列化器
                .build();
        /**
         * BeanSerializer序列化器   不包括Bean对象中null值，可以在Bean对象上加@JsonInclude(value = JsonInclude.Include.NON_NULL)注解
         * "{"bl":false,"id":1,"name":"someiii阿迪斯","pojoAnoEnum":"SYSTEM","pojoAnoEnumValue":null,"pojoTime":"2021-05-17 01:06:56","bi":771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121,"bd":8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423,"dt":"2021-05-17 01:06:56"}:
         */
        String pojoAnoSe = om.writeValueAsString(pojoAno); //date
        //UntypedObjectDeserializer，pojo,pojo中每一个字段都使用此反序列化器，pojoObj实际类型为LinkedHashMap
        Object pojoAnoObj = om.readValue(pojoAnoSe, Object.class);
        /**
         * BeanDeserializer反序列化器，PojoAno对象
         * PojoAno(bl=false, id=1, name=someiii阿迪斯, pojoAnoEnum=SYSTEM, pojoAnoEnumValue=null, pojoTime=2021-05-17T01:06:56, bi=771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121, bd=8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423, dt=Mon May 17 01:06:56 CST 2021)
         */
        ForSerializeTestPojoAno pojoAnoVal = om.readValue(pojoAnoSe, ForSerializeTestPojoAno.class);

        Map<String, Object> pojoMap = ReflectUtils.bean2Map(pojoAnoVal);


        System.out.println("basic");
    }


    public static void forRedisUse() throws Exception {

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.registerModule(JacksonUtils.defaultJavaTimeModule());

        /**
         * NullSerializer序列化器， null--->"null"
         */
        String nullSe = om.writeValueAsString(null);
        //"null"--->null
        Object nullObj = om.readValue(nullSe, Object.class);

        /**
         * Boolean类型，使用BooleanSerializer序列化器，true-->"true"; false--->"false"
         */
        String booleanSe = om.writeValueAsString(true);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，booleanObj的实际类型是Boolean
         */
        Object booleanObj = om.readValue(booleanSe, Object.class);
        //NumberDeserializers$BooleanDeserializer反序列化器，"true"--->true; "false"--->false
        Boolean booleanVal = om.readValue(booleanSe, Boolean.class);

        /**
         * Character类型，使用ToStringSerializer序列化器，'a'.toString()--->"\"a\""
         */
        String charSe = om.writeValueAsString('a');
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，charObj实际类型为String
         */
        Object charObj = om.readValue(charSe, Object.class);
        //NumberDeserializers$CharacterDeserializer反序列化器，"\"a\""--->'a'
        Character charVal = om.readValue(charSe, Character.class);

        /**
         * Byte类型，使用NumberSerializers$IntLikeSerializer序列化器，(Number)var.intValue(),...--->"1"
         */
        String byteSe = om.writeValueAsString((byte) 1);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，byteObj实际类型为Integer
         */
        Object byteObj = om.readValue(byteSe, Object.class);
        //NumberDeserializers$ByteDeserializer序列化器，"1"--->Byte
        Byte byteVal = om.readValue(byteSe, Byte.class);


        /**
         * Short类型，使用NumberSerializers$ShortSerializer序列化器，(Short)var.shortValue(),...--->"2"
         */
        String shortSe = om.writeValueAsString((short) 2);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，shortObj实际类型为Integer
         */
        Object shortObj = om.readValue(shortSe, Object.class);
        //NumberDeserializers$ShortDeserializer序列化器，"2"--->Short
        Short shortVal = om.readValue(shortSe, Short.class);


        /**
         * Integer类型，使用NumberSerializers$IntegerSerializer序列化器，(Integer)var.intValue(),...--->"3"
         */
        String integerSe = om.writeValueAsString(3);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，integerObj实际类型为Integer
         */
        Object integerObj = om.readValue(integerSe, Object.class);
        //NumberDeserializers$IntegerDeserializer, "3"--->Integer
        Integer integerVal = om.readValue(integerSe, Integer.class);


        /**
         * Long类型，使用NumberSerializers$LongSerializer序列化器，(Long)var.longValue(),...--->"123456789098777777"
         */
        String longSe = om.writeValueAsString(123456789098777777L);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，longObj实际类型为Long
         */
        Object longObj = om.readValue(longSe, Object.class);
        //NumberDeserializers$LongDeserializer, "123456789098777777"--->Long
        Long longVal = om.readValue(longSe, Long.class);


        /**
         * String类型，使用StringSerializer序列化器，"\"中daddsdsa\""
         */
        String stringSe = om.writeValueAsString("中daddsdsa");
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，stringObj实际类型为String
         */
        Object stringObj = om.readValue(stringSe, Object.class);
        //StringDeserializer反序列化器，"中daddsdsa"
        String stringVal = om.readValue(stringSe, String.class);


        /**
         * Float类型，使用NumberSerializers$FloatSerializer序列化器，(Float)var.floatValue(),...--->"1.2345667" 精度丢失
         */
        String floatSe = om.writeValueAsString(1.2345666666666666666675465645f);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，floatObj实际类型为Double，丢失精度后的值
         */
        Object floatObj = om.readValue(floatSe, Object.class);
        //NumberDeserializers$FloatDeserializer, 丢失精度后的值
        Float floatVal = om.readValue(floatSe, Float.class);


        /**
         * Double类型，使用NumberSerializers$DoubleSerializer序列化器，(Double)var.doubleValue(),...--->"121.32131231231232"  精度丢失
         */
        String doubleSe = om.writeValueAsString(121.321312312312312323123123123131231);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer，doubleObj实际类型为Double，丢失精度后的值
         */
        Object doubleObj = om.readValue(doubleSe, Object.class);
        //NumberDeserializers$DoubleDeserializer, 丢失精度后的值
        Double doubleVal = om.readValue(doubleSe, Double.class);



        BigInteger bi = new BigInteger("771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121");
        BigDecimal bd = new BigDecimal("8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423");
        /**
         * BigInteger类型，TypeWrappedSerializer->StdScalarSerializer->NumberSerializer
         * "["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121]"
         */
        String biSe = om.writeValueAsString(bi);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer->AsArrayTypeDeserializer->NumberDeserializers$BigIntegerDeserializer
         * biObj实际类型为BigInteger
         */
        Object biObj = om.readValue(biSe, Object.class);
        /**
         * TypeWrappedDeserializer->StdScalarDeserializer->AsArrayTypeDeserializer->NumberDeserializers$BigIntegerDeserializer
         */
        BigInteger biVal = om.readValue(biSe, BigInteger.class);

        /**
         * BigDecimal类型，TypeWrappedSerializer->StdScalarSerializer->NumberSerializer
         * "["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423]"
         */
        String bdSe = om.writeValueAsString(bd);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer->AsArrayTypeDeserializer->NumberDeserializers$BigDecimalDeserializer
         * bdObj实际类型为BigDecimal
         */
        Object bdObj = om.readValue(bdSe, Object.class);
        /**
         * TypeWrappedDeserializer->StdScalarDeserializer->AsArrayTypeDeserializer->NumberDeserializers$BigDecimalDeserializer
         */
        BigDecimal bdVal = om.readValue(bdSe, BigDecimal.class);


        LocalDateTime date = LocalDateTime.now();
        /**
         * LocalDateTime类型，使用LocalDateTimeSerializer序列化器(需要注册module)，根据formatter格式化成日期字符串--->"\"2021-05-16 22:05:41\""
         */
        String dateSe = om.writeValueAsString(date);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer
         * dateObj实际类型为String
         */
        Object dateObj = om.readValue(dateSe, Object.class);
        /**
         * LocalDateTimeDeserializer，将"2021-05-16 22:05:41"反序列化为LocalDateTime(使用对应的DateTimeFormatter)
         */
        LocalDateTime dateVal = om.readValue(dateSe, LocalDateTime.class);


        /**
         * Enum类型(没有加@JsonFormat,@JsonCreator)，使用EnumSerializer序列化器，PojoEnum.SYSTEM--->"\"SYSTEM\""
         */
        String pojoEnumSe = om.writeValueAsString(ForRedisTestPojoEnum.SYSTEM);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer
         * enumObj实际类型为String, "SYSTEM"
         */
        Object pojoEnumObj = om.readValue(pojoEnumSe, Object.class);
        /**
         * EnumDeserializer反序列化器，"SYSTEM"--->PojoEnum.SYSTEM
         */
        ForRedisTestPojoEnum pojoEnumVal = om.readValue(pojoEnumSe, ForRedisTestPojoEnum.class);


        /**
         * Enum with @JsonFormat、@JsonCreator，使用BeanSerializer序列化器
         * "{"value":"SYSTEM","text":"系统"}"
         */
        String pojoAnoEnumSe = om.writeValueAsString(ForRedisTestPojoAnoEnum.SYSTEM);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer->AsArrayTypeDeserializer
         * 判定"{"value":"SYSTEM","text":"系统"}"字符串没有类型信息而报错
         * TODO Enum with @JsonFormat、@JsonCreator 在序列化时没有写入类型信息
         */
        //Object pojoAnoEnumObj = om.readValue(pojoAnoEnumSe, Object.class);
        /**
         * FactoryBasedEnumDeserializer反序列化器
         * "{"value":"SYSTEM","text":"系统"}"这个字符串没有对应的Enum实例，返回null
         */
        ForRedisTestPojoAnoEnum pojoAnoEnumVal = om.readValue(pojoAnoEnumSe, ForRedisTestPojoAnoEnum.class);


        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("q中");
        /**
         * TypeWrappedSerializer,IndexedListSerializer,StringSerializer
         * "["java.util.ArrayList",["1","q中"]]"
         */
        String listSe = om.writeValueAsString(list);
        //... ArrayList<Object>
        Object listObj = om.readValue(listSe, Object.class);
        //... ArrayList<Object>
        List listVal = om.readValue(listSe, List.class);
        /**
         * 泛型类型..., ArrayList<String>
         */
        List<String> listGenericVal = om.readValue(listSe, new TypeReference<List<String>>(){});


        Date dt = new Date(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        //Date未使用@JsonFormat,Enum未使用@JsonFormat,@JsonCreator
        ForSerializeTestPojo pojo = ForSerializeTestPojo.builder()
                .id(1L)              //NumberSerializers$LongSerializer序列化器     NumberDeserializers$LongDeserializer反序列化器
                .bl(true)            //BooleanSerializer序列化器                    NumberDeserializers$BooleanDeserializer反序列化器
                .name("someiii阿迪斯")  //StringSerializer序列化器                    StringDeserializer反序列化器
                .pojoEnum(ForRedisTestPojoEnum.SYSTEM)  //EnumSerializer序列化器     EnumDeserializer反序列化器
                .pojoTime(date)          //使用注册的LocalDateTimeSerializer序列化器   LocalDateTimeDeserializer反序列化器
                .bi(bi)                //使用NumberSerializer序列化器                 NumberDeserializers$BigIntegerDeserializer反序列化器/...
                .bd(bd)              //使用NumberSerializer序列化器                   NumberDeserializers$BigIntegerDeserializer反序列化器/...
                .dt(dt)              //DateSerializer序列化器                        DateDeserializers$DateDeserializer反序列化器/...
                .build();
        /**
         * TypeWrappedSerializer->BeanSerializer  Pojo,BigInteger,BigDecimal,Date有类型
         * "["com.sc.sample.serialize.Pojo",{"bl":true,"id":1,"name":"someiii阿迪斯","pojoEnum":"SYSTEM","pojoTime":"2021-05-17 15:56:33","bi":["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],"bd":["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],"dt":["java.util.Date",1621238193752]}]"
         */
        String pojoSe = om.writeValueAsString(pojo);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer->AsArrayTypeDeserializer->BeanDeserializer
         * pojoObj实际类型为Pojo
         */
        Object pojoObj = om.readValue(pojoSe, Object.class);
        /**
         * TypeWrappedDeserializer->AsArrayTypeDeserializer->BeanDeserializer...
         * Pojo
         */
        ForSerializeTestPojo pojoVal = om.readValue(pojoSe, ForSerializeTestPojo.class);


        //Date使用@JsonFromat，Enum使用@JsonFormat，@JsonCreator
        ForSerializeTestPojoAno pojoAno = ForSerializeTestPojoAno.builder()
                .id(1L)                             //NumberSerializers$LongSerializer序列化器          NumberDeserializers$LongDeserializer反序列化器
                .bl(false)                          //BooleanSerializer序列化器                         NumberDeserializers$BooleanDeserializer反序列化器
                .name("someiii阿迪斯")               //StringSerializer序列化器                          StringDeserializer反序列化器
                .pojoAnoEnum(ForRedisTestPojoAnoEnum.SYSTEM.getValue()) //StringSerializer序列化器      StringDeserializer反序列化器
                .pojoTime(date)                     //注册的LocalDateTimeSerializer序列化器              LocalDateTimeDeserializer反序列化器
                .bi(bi)                             //NumberSerializer序列化器                          NumberDeserializers$BigIntegerDeserializer反序列化器/...
                .bd(bd)                             //NumberSerializer序列化器                          NumberDeserializers$BigIntegerDeserializer反序列化器/...
                .dt(dt)                             //DateSerializer，按@JsonFormat注解序列化            DateDeserializers$DateDeserializer反序列化器/...
                .build();
        /**
         * TypeWrappedSerializer->BeanSerializer  PojoAno,BigInteger,BigDecimal,Date有类型
         * "["com.sc.sample.serialize.PojoAno",{"bl":false,"id":1,"name":"someiii阿迪斯","pojoAnoEnum":"SYSTEM","pojoAnoEnumValue":null,"pojoTime":"2021-05-17 16:11:51","bi":["java.math.BigInteger",771123123123123123123213123213333333333333333333333333333333333313123123123123123213123123123123123123123121],"bd":["java.math.BigDecimal",8.9999011231312312312323123123123123123123123123123123123123123123123123123234434541353453645364356421432423],"dt":["java.util.Date","2021-05-17 16:11:51"]}]"
         */
        String pojoAnoSe = om.writeValueAsString(pojoAno); //date
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer->AsArrayTypeDeserializer->BeanDeserializer
         * pojoAnoObj实际类型是PojoAno
         */
        Object pojoAnoObj = om.readValue(pojoAnoSe, Object.class);
        /**
         * TypeWrappedDeserializer->AsArrayTypeDeserializer->BeanDeserializer...
         * PojoAno
         */
        ForSerializeTestPojoAno pojoAnoVal = om.readValue(pojoAnoSe, ForSerializeTestPojoAno.class);


        System.out.println("redis");
    }


    public static void  test1() throws Exception {
        LocalDateTime date = LocalDateTime.now();

        //不写类型,目前结论: 在不写类型情况下，LocalDateTime如果不采用JavaTimeModule来序列化，则解析报错
        ObjectMapper om1 = new ObjectMapper();
        /**
         * BeanSerializer
         * "{"minute":35,"dayOfMonth":17,"nano":553000000,"year":2021,"monthValue":5,"hour":16,"second":13,"dayOfWeek":"MONDAY","dayOfYear":137,"month":"MAY","chronology":{"id":"ISO","calendarType":"iso8601"}}"
         */
        String dateSe = om1.writeValueAsString(date);
        /**
         * UntypedObjectDeserializer序列化器
         * dateObj实际类型为LinkedHashMap
         */
        Object dateObj = om1.readValue(dateSe, Object.class);
        /**
         * BeanDeserializer反序列化器
         * 报异常，`java.time.LocalDateTime` (no Creators, like default construct, exist)...
         */
        //LocalDateTime dateVal = om1.readValue(dateSe, LocalDateTime.class);

        //写类型,目前结论: 在写类型情况下，LocalDateTime如果不采用JavaTimeModule来序列化，则解析报错
        ObjectMapper om2 = new ObjectMapper();
        om2.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om2.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        /**
         * BeanSerializer
         * "{"date":{"year":2021,"month":"MAY","day":17,"era":["java.time.chrono.IsoEra","CE"],"dayOfWeek":"MONDAY","dayOfYear":137,"leapYear":false,"chronology":{"id":"ISO","calendarType":"iso8601"},"monthValue":5,"dayOfMonth":17,"prolepticMonth":24256},"time":{"hour":16,"minute":41,"second":12,"nano":726000000},"dayOfWeek":"MONDAY","dayOfYear":137,"month":"MAY","monthValue":5,"dayOfMonth":17,"minute":41,"nano":726000000,"hour":16,"second":12,"year":2021,"chronology":["java.time.chrono.IsoChronology",{"id":"ISO","calendarType":"iso8601"}]}"
         */
        dateSe = om2.writeValueAsString(date);
        /**
         * TypeWrappedDeserializer->UntypedObjectDeserializer->AsArrayTypeDeserializer
         * 类型解析报错，LocalDateTime写入类型无法解析
         */
        //dateObj = om2.readValue(dateSe, Object.class);
        /**
         * BeanDeserializer反序列化器
         * 报异常，`java.time.LocalDateTime` (no Creators, like default construct, exist)...
         */
        //LocalDateTime dateVal = om1.readValue(dateSe, LocalDateTime.class);


        System.out.println("LocalDateTime在没有JavaTimeModule时的表现");
    }


    public static void main(String[] argv) throws Exception {

        theBasicUse();
        forRedisUse();
        test1();





    }


}
