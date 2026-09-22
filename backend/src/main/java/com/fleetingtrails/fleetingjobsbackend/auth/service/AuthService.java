package com.fleetingtrails.fleetingjobsbackend.auth.service;

import com.fleetingtrails.fleetingjobsbackend.auth.dto.AuthResponseDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.LoginRequestDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.SetPasswordRequestDto;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class AuthService {

    private static final String UNUSABLE_PASSWORD = "{noop}LOCKED";

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDto login(LoginRequestDto request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (matches(request.getPassword(), user.getPassword())) {
            return toAuthenticatedResponse(user);
        }

        if (matches(request.getPassword(), user.getOtp())) {
            return toPasswordSetupRequiredResponse(user);
        }

        throw new BadCredentialsException("Invalid email or password");
    }

    @Transactional
    public AuthResponseDto setPassword(SetPasswordRequestDto request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or OTP"));

        if (user.getOtp() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password has already been set for this account"
            );
        }

        if (!Objects.equals(request.getOtp(), user.getOtp())) {
            throw new BadCredentialsException("Invalid email or OTP");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setOtp(null);
        userRepository.save(user);

        return toAuthenticatedResponse(user);
    }

//    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String password = user.getPassword() != null ? user.getPassword() : UNUSABLE_PASSWORD;

        return user;
    }

    private boolean matches(String raw, String encoded) {
        return raw != null && encoded != null && passwordEncoder.matches(raw, encoded);
    }

    private AuthResponseDto toAuthenticatedResponse(UserEntity user) {
        UserEntity userDetails = loadUserByUsername(user.getEmail());
        String jwt = jwtService.generateToken(userDetails);

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwt);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setRequiresPasswordSetup(false);
        return response;
    }

    private AuthResponseDto toPasswordSetupRequiredResponse(UserEntity user) {
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(null);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setRequiresPasswordSetup(true);
        return response;
    }
}
