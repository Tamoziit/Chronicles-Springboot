package com.tamojit.chronicles.response;

import lombok.AllArgsConstructor;
import lombok.Data;

// For returning data to frontend
@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private Object data;
}
