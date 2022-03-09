package com.haenggu.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSimpleResponse {
    private String username;
    private String profileImage;

    @Builder
    public UserSimpleResponse(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }
}
