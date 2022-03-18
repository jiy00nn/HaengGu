package com.haenggu.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.haenggu.domain.entity.Users;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @Builder
    public UserSimpleResponse(Users user) {
        this.username = user.getUsername();
        this.profileImage = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/profile/")
                .path(user.getUserId().toString())
                .toUriString();
    }
}
