package com.sc.sample.dto.scan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sc.common.enums.ScanTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SampleScanUpdateDto {

    @NotNull(message = "id不能为空")
    private Long id;
    private String name;
    private ScanTypeEnum scanType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scanTime;

}
