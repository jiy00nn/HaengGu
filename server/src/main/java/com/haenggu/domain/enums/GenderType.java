package com.haenggu.domain.enums;

public enum GenderType {
    MALE("남성"),
    FEMALE("여성");

    private String value;

    GenderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
