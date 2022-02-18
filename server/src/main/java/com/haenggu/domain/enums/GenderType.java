package com.haenggu.domain.enums;

public enum GenderType {
    MALE("남자"),
    FEMALE("여자");

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
