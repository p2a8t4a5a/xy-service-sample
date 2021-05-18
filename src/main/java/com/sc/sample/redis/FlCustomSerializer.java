package com.sc.sample.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sc.common.exception.BizException;
import com.sc.common.utils.JacksonUtils;
import jdk.internal.org.objectweb.asm.TypeReference;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


@Slf4j
public class FlCustomSerializer {

    private ObjectMapper objectMapper = new ObjectMapper();
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public FlCustomSerializer() {
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.registerModule(JacksonUtils.defaultJavaTimeModule());
    }
    public FlCustomSerializer(boolean withoutType) {
        if(!withoutType) {
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        }
        objectMapper.registerModule(JacksonUtils.defaultJavaTimeModule());
    }

    public Object deserialize(String value) {
        try {
            return this.objectMapper.readValue(value, Object.class);
        } catch (Exception ex) {
            throw new BizException(() -> "Could not read JSON: " + ex.getMessage());
        }
    }

    public <T> T deserialize(String value, Class<T> clz) {
        try {
            return this.objectMapper.readValue(value, clz);
        } catch (Exception ex) {
            throw new BizException(() -> "Could not read JSON: " + ex.getMessage());
        }
    }

    /*public <T> T deserialize(String value, TypeReference type) {
        try {
            return this.objectMapper.readValue(value, type);
        } catch (Exception ex) {
            throw new BizException(() -> "Could not read JSON: " + ex.getMessage());
        }
    }*/

    public <T> T deserialize(byte[] bytes, Class<T> clz) {
        try {
            return this.objectMapper.readValue(bytes, 0, bytes.length, clz);
        } catch (Exception ex) {
            throw new BizException(() -> "Could not read JSON: " + ex.getMessage());
        }
    }

    public String serialize(Object value) {
        try {
            return this.objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            throw new BizException(() -> "Could not write JSON: " + ex.getMessage());
        }
    }

    public byte[] serializeAsBytes(Object value) {
        try {
            return this.objectMapper.writeValueAsBytes(value);
        } catch (Exception ex) {
            throw new BizException(() -> "Could not write JSON: " + ex.getMessage());
        }
    }

}
