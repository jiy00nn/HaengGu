package com.haenggu.controller;

import com.haenggu.domain.entity.Users;
import com.haenggu.http.response.LoginSuccessResponse;
import com.haenggu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;


@RestController
@RequestMapping("/api/oauth")
public class OAuthController {

    UserService userService;

    @Autowired
    public OAuthController(UserService userService) {
        this.userService = userService;
    }

    @ApiIgnore
    @GetMapping("/success/{id}")
    public ResponseEntity<?> successLogin(@PathVariable("id") UUID id) {
        Users user = userService.getUser(id);
        return ResponseEntity.ok(new LoginSuccessResponse(user.getUserId(), user.getRoleType()));
    }
}
