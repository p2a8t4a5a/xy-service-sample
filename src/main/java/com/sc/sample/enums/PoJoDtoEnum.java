package com.sc.sample.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PoJoDtoEnum {
    SYSTEM("SYSTEM", "系统");

    @EnumValue
    private String value;

    private String text;

    PoJoDtoEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @JsonCreator
    public static PoJoDtoEnum getByValue(String value) {
        return Arrays.stream(PoJoDtoEnum.values()).filter(scan -> scan.getValue().equals(value)).findFirst().orElse(null);
    }

}
