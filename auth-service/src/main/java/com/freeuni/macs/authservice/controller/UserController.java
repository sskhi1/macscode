package com.freeuni.macs.authservice.controller;

import com.freeuni.macs.authservice.mapper.UserMapper;
import com.freeuni.macs.authservice.model.api.AuthResponse;
import com.freeuni.macs.authservice.model.api.SignInRequest;
import com.freeuni.macs.authservice.model.api.SignUpRequest;
import com.freeuni.macs.authservice.model.api.UserDTO;
import com.freeuni.macs.authservice.model.db.User;
import com.freeuni.macs.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${server.context-path}")
@Slf4j
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
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

    @GetMapping(value = "/users/{username}", produces = "application/json")
    public UserDTO getUser(
            @PathVariable(name = "username") @Parameter(description = "user name", in = ParameterIn.PATH) String username
    ) {
        return userMapper.entityToDTO(userService.getUser(username));
    }

    @PutMapping("/users/update/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable String username,
            @RequestBody UserDTO userDto) {
        userService.updateUser(username, userDto);
        return ResponseEntity.ok("User updated successfully.");
    }

    @GetMapping("/users/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{username}")
    public void deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
    }

    @PatchMapping("/{username}/make-admin")
    public void makeUserAdmin(@PathVariable String username) {
        userService.makeUserAdmin(username);
    }
}
