package com.haenggu.controller;

import com.haenggu.domain.entity.Users;
import com.haenggu.http.request.SignUpRequest;
import com.haenggu.http.response.SchoolNameListResponse;
import com.haenggu.http.response.SchoolListResponse;
import com.haenggu.http.response.UserResponse;
import com.haenggu.service.SchoolService;
import com.haenggu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "user", description = "사용자 정보 관련 API")
@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;
    SchoolService schoolService;

    @Autowired
    public UserController(UserService userService, SchoolService schoolService) {
        this.userService = userService;
        this.schoolService = schoolService;
    }

    @Operation(summary = "학교 이름 검색", description = "문자열을 통해 학교 정보를 검색합니다", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "학교 이름 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchoolNameListResponse.class)))
            })
    @GetMapping("/schools")
    public ResponseEntity<SchoolNameListResponse> getSchoolName(@PageableDefault(size = 20) @SortDefault(sort = "schoolName") Pageable pageable,
                                                                @Parameter(name = "school-name", in = ParameterIn.QUERY, description = "학교 이름") @RequestParam(value = "school-name", required = false) String school){
        if (school == null) {
            return ResponseEntity.ok(new SchoolNameListResponse(pageable, schoolService.findSchoolNames(pageable)));
        }
        return ResponseEntity.ok(new SchoolNameListResponse(pageable, schoolService.findSchoolNamesByString(school, pageable)));
    }

    @Operation(summary = "학과 이름 검색", description = "문자열을 통해 학과 정보를 검색합니다", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "학과 이름 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SchoolListResponse.class)))
            })
    @GetMapping("/majors")
    public ResponseEntity<SchoolListResponse> getMajorName(@PageableDefault(size = 20) Pageable pageable,
                                                           @Parameter(name = "school-name", in = ParameterIn.QUERY, description = "학교 이름") @RequestParam(value = "school-name") String school,
                                                           @Parameter(name = "major-name", in = ParameterIn.QUERY, description = "학과 이름") @RequestParam(value = "major-name", required = false) String major){
        if (major == null) {
            return ResponseEntity.ok(new SchoolListResponse(pageable, schoolService.findSchoolBySchoolName(school, pageable)));
        }
        return ResponseEntity.ok(new SchoolListResponse(pageable, schoolService.findSchoolBySchoolNameAndMajorName(school, major, pageable)));
    }


    @Operation(summary = "회원 가입 유저 정보 추가", description = "회원 가입시 유저 정보를 추가합니다.", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "행사 정보 수정 성공",
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
                .birthday(signUpRequest.getBirthday())
                .eventTag(signUpRequest.getCategoryTag())
                .regionTag(signUpRequest.getRegionTag())
                .build();
        UserResponse response = UserResponse.builder()
                .user(userService.updateUser(idx,data)).build();
        return ResponseEntity.ok(response);
    }
}
