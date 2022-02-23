package com.haenggu.auth.jwt;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Token {
    private String roleType;
    private String token;
    private String refreshToken;

    @Builder
    public Token(String token, String refreshToken, String roleType) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.roleType = roleType;
    }
}
