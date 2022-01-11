package com.haenggu.domain.enums;

public enum CategoryType {
    CLUB("동아리행사"),
    JOBFAIR("잡페어"),
    CONFERENCE("컨퍼런스"),
    EXHIBITION("전시"),
    FESTIVAL("페스티벌"),
    CONCERT("콘서트"),
    THEATER("연극/뮤지컬"),
    ETC("기타");

    private String value;

    CategoryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
