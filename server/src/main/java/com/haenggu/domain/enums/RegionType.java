package com.haenggu.domain.enums;

public enum RegionType {
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    GWANJU("광주"),
    INCHEON("인천"),
    DAEJEON("대전"),
    ULSAN("울산"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGCHEONG("충청"),
    JEOLLABUK("전라"),
    GYEONGSANG("경상"),
    JEJU("제주");

    private String value;

    RegionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
