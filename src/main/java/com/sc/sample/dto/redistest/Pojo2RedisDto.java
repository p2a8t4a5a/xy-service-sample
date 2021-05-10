package com.sc.sample.dto.redistest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sc.sample.enums.PoJoDtoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pojo2RedisDto {
    private String name;
    private String pojoType; //PojoDtoEnum.value
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pojoTime;
}
