package com.haenggu.domain.enums;

import org.springframework.http.HttpMethod;

public enum SocialType {
    KAKAO(
            "kakao",
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.GET
    ),
    GOOGLE(
            "google",
            "https://www.googleapis.com/oauth2/v3/userinfo",
            HttpMethod.GET
    );

    private String value;
    private String userInfoUrl;
    private HttpMethod method;

    SocialType(String value, String userInfoUrl, HttpMethod method) {
        this.value = value;
        this.userInfoUrl = userInfoUrl;
        this.method = method;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getSocialName() {
        return this.value;
    }

    public String getUserInfoUrl() {
        return this.userInfoUrl;
    }
}
