package com.sc.sample.dto.scan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sc.common.enums.ScanTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ScanAddDto {

    @NotBlank(message = "name不能为空")
    private String name;
    @NotNull(message = "scanType不能为空")
    private ScanTypeEnum scanType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "scanTime不能为空")
    private LocalDateTime scanTime;


}
