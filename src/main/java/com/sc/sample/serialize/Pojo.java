package com.sc.sample.serialize;

import com.sc.sample.enums.PojoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pojo {
    private Long id;
    private String name;
    private PojoEnum pojoEnum;
    private LocalDateTime pojoTime;
}
