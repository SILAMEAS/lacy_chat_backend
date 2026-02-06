package com.lacy.chat.modules.user.controller;

import com.lacy.chat.modules.user.dto.res.UserResponse;
import com.lacy.chat.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        log.error(Objects.requireNonNull(authentication.getCredentials()).toString());
        return ResponseEntity.ok(userService.findUserByEmail("sila@gmail.com"));
    }
}

