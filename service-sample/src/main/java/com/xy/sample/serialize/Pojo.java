package com.xy.sample.serialize;

import com.xy.sample.redis.enums.PojoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pojo {
    private Boolean bl;
    private Long id;
    private String name;
    private PojoEnum pojoEnum;
    private LocalDateTime pojoTime;
    private BigInteger bi;
    private BigDecimal bd;
    private Date dt;
}
