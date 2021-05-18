package com.sc.sample.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.common.utils.JacksonUtils;
import com.sc.common.redis.FlCustomSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory/*, ObjectMapper om*/) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //ObjectMapper omToUse = om.copy();
        ObjectMapper omToUse = new ObjectMapper();
        omToUse.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        omToUse.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        omToUse.registerModule(JacksonUtils.defaultJavaTimeModule());

        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(omToUse);

        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public FlCustomSerializer flCustomSerializer() {
        return new FlCustomSerializer();
    }


    @Bean
    @Qualifier("customRedisTemplate")//the same as StringRedisTemplate
    public RedisTemplate<String, String> customRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> stringRedisTemplate = new RedisTemplate<>();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);

        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        stringRedisTemplate.setKeySerializer(stringSerializer);// key序列化
        stringRedisTemplate.setValueSerializer(stringSerializer);// value序列化
        stringRedisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        stringRedisTemplate.setHashValueSerializer(stringSerializer);// Hash value序列化

        stringRedisTemplate.afterPropertiesSet();
        return stringRedisTemplate;
    }


    @Bean
    public RedisTemplate<String, byte[]> genericRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, byte[]> genericRedisTemplate = new RedisTemplate<>();
        genericRedisTemplate.setConnectionFactory(redisConnectionFactory);

        RedisSerializer<?> stringSerializer = new StringRedisSerializer();

        genericRedisTemplate.setEnableDefaultSerializer(false);
        genericRedisTemplate.setKeySerializer(stringSerializer);// key序列化
        genericRedisTemplate.setValueSerializer(null);// value序列化，设置为null
        genericRedisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        genericRedisTemplate.setHashValueSerializer(null);// Hash value序列化，设置为null

        return genericRedisTemplate;
    }




    //存在问题，see SampleRedisTestController.getNumber
    //key-value(value是Number类型); key-hash(hash-value是Number类型); key-lists(lists每个元素是Number)
    //eg: Byte,Short,Integer,Long,Float,Double,BigInteger,BigDecimal
    @Deprecated
    @Bean
    public RedisTemplate<String, Number> numberRedisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                             ObjectMapper om) {
        RedisTemplate<String, Number> numberRedisTemplate = new RedisTemplate<>();
        numberRedisTemplate.setConnectionFactory(redisConnectionFactory);

        ObjectMapper omToUse = om.copy();
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Number> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Number.class);
        jackson2JsonRedisSerializer.setObjectMapper(omToUse);

        numberRedisTemplate.setKeySerializer(stringSerializer);// key序列化
        numberRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        numberRedisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        numberRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        numberRedisTemplate.afterPropertiesSet();

        return numberRedisTemplate;
    }


}
