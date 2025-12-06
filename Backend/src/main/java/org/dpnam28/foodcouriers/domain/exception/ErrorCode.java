package org.dpnam28.foodcouriers.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("Internal server error", 500),
    USER_NOT_FOUND("User not found", 404),
    USER_ALREADY_EXISTS("User already exists", 409),
    LOCATION_NOT_FOUND("Location not found", 404),
    INVALID_PASSWORD("Invalid password", 400),
    EMAIL_NOT_VALID("Email is not valid", 400),
    ARGUMENT_IS_REQUIRED("{arg} is required", 400),
    ;
    private final String message;
    private final int code;

    ErrorCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static ErrorCode fromMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Message must not be null");
        }
        for (ErrorCode errorCode : values()) {
            if (errorCode.message.equalsIgnoreCase(message.trim())) {
                return errorCode;
            }
        }
        throw new IllegalArgumentException("No matching ErrorCode for message: " + message);
    }

    public String formatMessage(String arg) {
        if (arg == null) {
            return message;
        }
        return message.replace("{arg}", arg.trim());
    }
}
