package com.haenggu.controller;

import com.haenggu.controller.examples.UserControllerExample;
import com.haenggu.domain.entity.School;
import com.haenggu.domain.entity.Users;
import com.haenggu.http.request.SignUpRequest;
import com.haenggu.http.response.UserResponse;
import com.haenggu.service.SchoolService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "user", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("/api/users")
public class UserController extends UserControllerExample {
    UserService userService;
    SchoolService schoolService;

    @Autowired
    public UserController(UserService userService, SchoolService schoolService) {
        this.userService = userService;
        this.schoolService = schoolService;
    }


    @Operation(summary = "학과 이름 검색", description = "문자열을 통해 학과 정보를 검색합니다", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "학과 이름 조회 성공",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class),
                                 examples = @ExampleObject(value = MAJOR_SEARCH_SUCCESS)))

            })
    @GetMapping("/majors")
    public ResponseEntity<List<School>> getMajorName(@Parameter(name = "school-name", in = ParameterIn.QUERY, description = "학교 이름") @RequestParam(value = "school-name") String school){
        return ResponseEntity.ok(schoolService.findSchoolBySchoolNameAndMajorName(school));
    }


    @Operation(summary = "회원 가입된 유저 정보 수정", description = "유저 정보를 수정합니다.", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
            })
    @PatchMapping
    public ResponseEntity<UserResponse> patchUser(@RequestBody SignUpRequest signUpRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID idx = UUID.fromString(authentication.getPrincipal().toString());
        Users data = Users.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .school(schoolService.findSchoolById(signUpRequest.getDeptId()))
                .gender(signUpRequest.getGender())
                .grade(signUpRequest.getGrade())
                .mbti(signUpRequest.getMbti())
                .birthday(signUpRequest.getBirthday())
                .eventTag(signUpRequest.getCategoryTag())
                .regionTag(signUpRequest.getRegionTag())
                .build();
        return ResponseEntity.ok(UserResponse.builder()
                .user(userService.updateUser(idx,data)).build());
    }
}
