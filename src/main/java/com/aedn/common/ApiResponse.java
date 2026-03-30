package com.aedn.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ErrorInfo error;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> failure(String message, String code, String details) {
        return new ApiResponse<>(false, message, null, new ErrorInfo(code, details));
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ErrorInfo {
    private String code;
    private String details;
}
