package com.fleetingtrails.fleetingjobsbackend.auth.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.dto.AuthResponseDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.LoginRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.SetPasswordRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/set-password")
    public ResponseEntity<AuthResponseDto> setPassword(@Valid @RequestBody SetPasswordRequestDto request) {
        return ResponseEntity.ok(authService.setPassword(request));
    }
}
