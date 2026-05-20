package com.realworld.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Valid
    @JsonProperty("user")
    private UpdateUserDto user;

    @Data
    public static class UpdateUserDto {
        private String email;
        private String password;
        private String username;
        private String bio;
        private String image;
    }
}
