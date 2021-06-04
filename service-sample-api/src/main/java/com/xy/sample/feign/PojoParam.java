package com.xy.sample.feign;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
public class PojoParam {
    private Long id;
    private String name;
    private LocalDateTime dt;
}
