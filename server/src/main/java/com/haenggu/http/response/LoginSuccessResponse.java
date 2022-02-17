package com.haenggu.http.response;

import com.haenggu.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoginSuccessResponse {
    UUID user_id;
    RoleType roleType;
}
