package com.fleetingtrails.fleetingjobsbackend.auth.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.dto.AuthResponseDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.ConfirmEmailRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.GoogleSignupRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.LinkedInSignupRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.LoginRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.SetPasswordRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.SignupRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.SignupResponseDto;
import com.fleetingtrails.fleetingjobsbackend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<AuthResponseDto> confirmEmail(@Valid @RequestBody ConfirmEmailRequestDto request) {
        return ResponseEntity.ok(authService.confirmEmail(request));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<AuthResponseDto> confirmEmailFromLink(@RequestParam("token") String token) {
        return ResponseEntity.ok(authService.confirmEmailToken(token));
    }

    @PostMapping("/signup/google")
    public ResponseEntity<AuthResponseDto> signupWithGoogle(@Valid @RequestBody GoogleSignupRequestDto request) {
        return ResponseEntity.ok(authService.signupWithGoogle(request));
    }

    @PostMapping("/signup/linkedin")
    public ResponseEntity<AuthResponseDto> signupWithLinkedIn(@Valid @RequestBody LinkedInSignupRequestDto request) {
        return ResponseEntity.ok(authService.signupWithLinkedIn(request));
    }

    @PostMapping("/set-password")
    public ResponseEntity<AuthResponseDto> setPassword(@Valid @RequestBody SetPasswordRequestDto request) {
        return ResponseEntity.ok(authService.setPassword(request));
    }
}
