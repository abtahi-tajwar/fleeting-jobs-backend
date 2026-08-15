package com.fleetingtrails.fleetingjobsbackend.auth.service;

import com.fleetingtrails.fleetingjobsbackend.auth.dto.AuthResponseDto;
import com.fleetingtrails.fleetingjobsbackend.auth.dto.LoginRequestDto;
import com.fleetingtrails.fleetingjobsbackend.common.exception.ResourceNotFoundException;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.mapper.UserMapper;
import com.fleetingtrails.fleetingjobsbackend.user.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;


    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       @Lazy AuthenticationManager authenticationManager,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }


    public AuthResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwt);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }


    public void seedUser(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) return;

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPassword(new BCryptPasswordEncoder().encode(rawPassword));
        user.setRole(com.fleetingtrails.fleetingjobsbackend.user.enums.Role.ADMIN);
        userRepository.save(user);
        System.out.println("Seeded admin user: " + email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}