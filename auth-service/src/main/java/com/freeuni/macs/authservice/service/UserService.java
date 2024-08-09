package com.freeuni.macs.authservice.service;

import com.freeuni.macs.authservice.exception.UserAuthException;
import com.freeuni.macs.authservice.model.api.AuthResponse;
import com.freeuni.macs.authservice.model.api.SignInRequest;
import com.freeuni.macs.authservice.model.api.UserDTO;
import com.freeuni.macs.authservice.model.db.User;
import com.freeuni.macs.authservice.repository.UserRepository;
import com.freeuni.macs.authservice.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthResponse validateUser(SignInRequest signInRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new UserAuthException("Invalid credentials");
        }
        User user = userRepository.
                findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new UserAuthException(
                        "User with username {} does not exist", signInRequest.getUsername()
                ));
        String jwt = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthResponse registerUser(User user) {
        Optional<User> userDB = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (userDB.isPresent()) {
            throw new UserAuthException("Account with email {} already exists.", user.getEmail());
        }
        Optional<User> userDB1 = userRepository.findByUsername(user.getUsername());
        if (userDB1.isPresent()) {
            throw new UserAuthException("Account with username {} already exists.", user.getUsername());
        }
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwt)
                .build();
    }

    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public void updateUser(String username, UserDTO userDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        userRepository.save(user);
    }
}
