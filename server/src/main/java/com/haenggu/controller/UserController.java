package com.haenggu.controller;

import com.haenggu.controller.examples.UserControllerExample;
import com.haenggu.domain.entity.School;
import com.haenggu.domain.entity.UserImage;
import com.haenggu.domain.entity.Users;
import com.haenggu.http.request.SignUpRequest;
import com.haenggu.http.response.GeneralResponse;
import com.haenggu.http.response.UploadFileResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @Operation(summary = "사용자 정보 조회", description = "사용자의 상세 정보를 조회합니다", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))

            })
    @GetMapping
    public ResponseEntity<UserResponse> getUserInfo() {
        return ResponseEntity.ok(userService.getUser());
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
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralResponse.class)))
            })
    @PatchMapping
    public ResponseEntity<GeneralResponse> patchUser(@RequestBody SignUpRequest signUpRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID idx = UUID.fromString(authentication.getPrincipal().toString());
        Users data = Users.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .description(signUpRequest.getDescription())
                .school(schoolService.findSchoolById(signUpRequest.getDeptId()))
                .gender(signUpRequest.getGender())
                .grade(signUpRequest.getGrade())
                .mbti(signUpRequest.getMbti())
                .birthday(signUpRequest.getBirthday())
                .eventTag(signUpRequest.getCategoryTag())
                .regionTag(signUpRequest.getRegionTag())
                .build();
        userService.updateUser(idx,data);
        return ResponseEntity.ok(GeneralResponse.of(HttpStatus.OK, "사용자 정보 수정에 성공하였습니다."));
    }

    @Operation(summary = "사용자 프로필 사진 등록", description = "사용자의 프로필 사진을 등록합니다.", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 프로필 이미지 등록 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadFileResponse.class)))
            })
    @PostMapping("/upload-image")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestBody MultipartFile file) {
        UserImage result = userService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/profile")
                .toUriString();

        return ResponseEntity.ok(UploadFileResponse.builder()
                .fileName(result.getOriginalName()).fileDownloadUri(fileDownloadUri)
                .fileType(result.getMimetype()).size(result.getSize())
                .build());
    }

    @Operation(summary = "사용자 프로필 이미지 조회", description = "토큰을 이용하여 사용자 프로필 사진을 조회합니다.", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 프로필 이미지 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = byte.class),
                                    examples = @ExampleObject(value = "byte value")))
            })
    @GetMapping("/profile")
    public @ResponseBody ResponseEntity<?> downloadFile(){
        UserImage image = userService.loadFileAsByte();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image.getData());
    }

    @Operation(summary = "사용자 프로필 이미지 조회", description = "사용자 프로필 이미지 ID 값을 통해 조회를 합니다.", tags = "user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 프로필 이미지 조회 성공",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = byte.class),
                                    examples = @ExampleObject(value = "byte value")))
            })
    @GetMapping("/profile/{idx}")
    public @ResponseBody ResponseEntity<?> userImage(@PathVariable("idx") UUID id){
        UserImage image = userService.loadFileAsByte(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image.getData());
    }
}
