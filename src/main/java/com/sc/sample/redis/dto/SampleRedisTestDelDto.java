package com.sc.sample.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class SampleRedisTestDelDto {

    @NotBlank(message = "key不能为空")
    private String key;

}
