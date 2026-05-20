package com.realworld.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @Valid
    @NotNull(message = "user is required")
    private UserDto user;

    @Data
    public static class UserDto {
        @NotBlank(message = "username is required")
        private String username;

        @NotBlank(message = "email is required")
        @Email(message = "email format is invalid")
        private String email;

        @NotBlank(message = "password is required")
        private String password;
    }
}
