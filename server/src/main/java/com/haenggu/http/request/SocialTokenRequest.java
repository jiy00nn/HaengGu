package com.haenggu.http.request;

import com.haenggu.domain.enums.SocialType;
import lombok.Getter;

@Getter
public class SocialTokenRequest {
    String code;
    SocialType provider;
}
