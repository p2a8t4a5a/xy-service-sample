package com.sc.sample.entity.scan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sc.common.entity.BaseEntity;
import com.sc.common.enums.ScanTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
public class SampleScan extends BaseEntity<Long> {

    private String name;
    private ScanTypeEnum scanType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scanTime;

}
