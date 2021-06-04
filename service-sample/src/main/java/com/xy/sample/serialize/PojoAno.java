package com.xy.sample.serialize;

import com.fasterxml.jackson.annotation.JsonFormat;
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
//@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PojoAno {
    private Boolean bl;
    private Long id;
    private String name;
    //private PojoAnoEnum pojoAnoEnum;
    private String pojoAnoEnum;  //使用PojoAnoEnum的value值，@see ScanTypeEnum中说明
    private String pojoAnoEnumValue;
    private LocalDateTime pojoTime;
    private BigInteger bi;
    private BigDecimal bd;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date dt;
}
