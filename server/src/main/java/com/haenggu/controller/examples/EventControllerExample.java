package com.haenggu.controller.examples;

public class EventControllerExample {
    public static final String EVENT_DELETE_SUCCESS    = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"행사 정보 삭제에 성공하였습니다.\"\n" +
            "}";
    public static final String EVENT_NOT_FOUND         = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"접근할 수 없는 아이디값입니다.\"\n" +
            "}";
    public static final String EVENT_TAG_LIST          = "[\n" +
            "    \"백신패스\",\n" +
            "    \"온라인\",\n" +
            "    \"방역패스\",\n" +
            "    \"언택트\",\n" +
            "    \"소프트웨어\"\n" +
            "]";
}
