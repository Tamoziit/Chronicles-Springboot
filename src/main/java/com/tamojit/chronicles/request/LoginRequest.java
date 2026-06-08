package com.tamojit.chronicles.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank // email not empty
    private String email;
    @NotBlank // password not empty
    private String password;
}
