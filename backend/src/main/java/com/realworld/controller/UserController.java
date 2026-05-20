package com.realworld.controller;

import com.realworld.common.annotation.AuthRequired;
import com.realworld.dto.request.LoginRequest;
import com.realworld.dto.request.RegisterRequest;
import com.realworld.dto.request.UpdateUserRequest;
import com.realworld.dto.response.UserResponse;
import com.realworld.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/users/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/user")
    @AuthRequired
    public UserResponse getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.getCurrentUser(userId);
    }

    @PutMapping("/user")
    @AuthRequired
    public UserResponse updateUser(HttpServletRequest request, @Valid @RequestBody UpdateUserRequest updateRequest) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return userService.updateUser(userId, updateRequest);
    }
}
