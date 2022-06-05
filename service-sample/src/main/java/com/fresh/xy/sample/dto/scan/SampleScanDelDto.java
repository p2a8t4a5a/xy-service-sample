package com.fresh.xy.sample.dto.scan;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SampleScanDelDto {
    @NotNull(message = "id不能为空")
    private Long id;
}
