package com.xy.sample.rocketmq.tx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sc.common.enums.ScanTypeEnum;
import com.sc.common.rmq.tx.RmqTxModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanRmqTxModel extends RmqTxModel {

    private String name;
    private ScanTypeEnum scanType;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scanTime;

}
