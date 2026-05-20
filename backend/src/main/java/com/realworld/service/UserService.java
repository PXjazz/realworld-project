package com.realworld.service;

import com.realworld.dto.request.LoginRequest;
import com.realworld.dto.request.RegisterRequest;
import com.realworld.dto.request.UpdateUserRequest;
import com.realworld.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);
    UserResponse login(LoginRequest request);
    UserResponse getCurrentUser(Long userId);
    UserResponse updateUser(Long userId, UpdateUserRequest request);
}
