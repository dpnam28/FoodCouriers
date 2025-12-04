package org.dpnam28.foodcouriers.domain.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("User not found", 404),
    USER_ALREADY_EXISTS("User already exists", 409);

    private final String message;
    private final int code;

    ErrorCode(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
