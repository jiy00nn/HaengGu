package com.haenggu.controller;

import com.haenggu.domain.entity.Event;
import com.haenggu.domain.entity.Users;
import com.haenggu.domain.enums.RoleType;
import com.haenggu.http.request.SignUpRequest;
import com.haenggu.http.response.GeneralResponse;
import com.haenggu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "user", description = "행사 정보 관련 API")
@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원 가입 유저 정보 추가", description = "회원 가입시 유저 정보를 추가합니다.", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "행사 정보 수정 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Users.class)))
            })
    @PutMapping("/{idx}")
    public ResponseEntity<Users> putUser(@Parameter(name = "idx", in = ParameterIn.PATH, description = "수정할 행사의 아이디") @PathVariable("idx") UUID idx, @RequestBody SignUpRequest signUpRequest) {
        Users data = Users.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .deptId(signUpRequest.getDeptId())
                .gender(signUpRequest.getGender())
                .grade(signUpRequest.getGrade())
                .birthday(signUpRequest.getBirthday())
                .eventTag(signUpRequest.getCategoryTag())
                .regionTag(signUpRequest.getRegionTag())
                .build();
        Users user = userService.updateUser(idx, data);
        return ResponseEntity.ok(user);
    }
}
