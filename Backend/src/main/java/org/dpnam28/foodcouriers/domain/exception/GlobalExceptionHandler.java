package org.dpnam28.foodcouriers.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.dpnam28.foodcouriers.domain.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error(e.toString());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getCode()).body(ApiResponse.apiResponse(errorCode));
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException e) {
        log.error(e.toString());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getCode()).body(ApiResponse.apiResponse(errorCode));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.toString());
        ErrorCode errorCode;
        String responseMessage;
        String defaultMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        try {
            log.info("Field validation message: {}", defaultMessage);
            errorCode = ErrorCode.fromMessage(defaultMessage);
            responseMessage = defaultMessage;
        } catch (IllegalArgumentException ex) {
            if (defaultMessage != null && defaultMessage.toLowerCase().endsWith("is required")) {
                String arg = defaultMessage.substring(0, defaultMessage.toLowerCase().lastIndexOf("is required")).trim();
                errorCode = ErrorCode.ARGUMENT_IS_REQUIRED;
                responseMessage = errorCode.formatMessage(arg);
            } else {
                errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
                responseMessage = errorCode.getMessage();
            }
        }
        return ResponseEntity.status(errorCode.getCode()).body(ApiResponse.apiResponse(errorCode, responseMessage));
    }
}
