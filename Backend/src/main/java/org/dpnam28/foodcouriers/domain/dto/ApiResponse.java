package org.dpnam28.foodcouriers.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dpnam28.foodcouriers.domain.exception.ErrorCode;

import java.util.Map;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> apiResponseSuccess(String message,T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<Object> apiResponse(ErrorCode errorCode) {
        return ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ApiResponse<Object> apiResponse(ErrorCode errorCode, String customMessage) {
        return ApiResponse.builder()
                .code(errorCode.getCode())
                .message(customMessage == null ? errorCode.getMessage() : customMessage)
                .build();
    }
}
