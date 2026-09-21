package com.fleetingtrails.fleetingjobsbackend.user.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIGetResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import com.fleetingtrails.fleetingjobsbackend.user.mapper.UserMapper;
import com.fleetingtrails.fleetingjobsbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<APIPostResponse<UserResponseDto>> createUser(@RequestBody UserCreateDto userCreateDto) {
        UserResponseDto createdUser = userService.createUser(userCreateDto);
        APIPostResponse<UserResponseDto> response = APIPostResponse.success(createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIListResponse<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        APIListResponse<UserResponseDto> response = APIListResponse.success(users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getProfile (
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity.ok(userMapper.toResponseDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIGetResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        APIGetResponse<UserResponseDto> response = APIGetResponse.success(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIGetResponse<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateDto userUpdateDto) {
        UserResponseDto updatedUser = userService.updateUser(id, userUpdateDto);
        APIGetResponse<UserResponseDto> response = APIGetResponse.success(updatedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}