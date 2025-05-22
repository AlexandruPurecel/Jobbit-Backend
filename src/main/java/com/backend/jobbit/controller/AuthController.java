package com.backend.jobbit.controller;

import com.backend.jobbit.config.security.JwtUtil;
import com.backend.jobbit.dto.userDto.UserLoginDto;
import com.backend.jobbit.persistence.model.User;
import com.backend.jobbit.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User user = userRepo.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok().body(Map.of("token", token));
    }
}
