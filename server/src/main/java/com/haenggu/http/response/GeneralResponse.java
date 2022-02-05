package com.haenggu.http.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralResponse {
    private final int code;
    private final String message;

    private GeneralResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static GeneralResponse of(HttpStatus httpStatus, String message) {
        return new GeneralResponse(httpStatus.value(), message);
    }
}

