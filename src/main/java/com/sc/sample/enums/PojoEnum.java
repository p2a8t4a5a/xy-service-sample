package com.sc.sample.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum PojoEnum {
    SYSTEM("SYSTEM", "系统");

    private String value;

    private String text;

    PojoEnum(String value, String text) {
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
    public static PojoEnum getByValue(String value) {
        return Arrays.stream(PojoEnum.values()).filter(scan -> scan.getValue().equals(value)).findFirst().orElse(null);
    }

}
