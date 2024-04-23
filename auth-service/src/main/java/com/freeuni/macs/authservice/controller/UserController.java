package com.freeuni.macs.authservice.controller;

import com.freeuni.macs.authservice.mapper.UserMapper;
import com.freeuni.macs.authservice.model.api.AuthResponse;
import com.freeuni.macs.authservice.model.api.SignInRequest;
import com.freeuni.macs.authservice.model.api.SignUpRequest;
import com.freeuni.macs.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${server.context-path}")
@Slf4j
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<AuthResponse> userSignIn(@Valid @RequestBody final SignInRequest signInRequest) {
        log.info("Signing in...");
        return ResponseEntity.ok(userService.validateUser(signInRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> userSignUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        log.info("Signing up...");
        AuthResponse authResponse = userService.registerUser(userMapper.registerDTOToEntity(signUpRequest));
        return ResponseEntity.ok(authResponse);
    }
}
