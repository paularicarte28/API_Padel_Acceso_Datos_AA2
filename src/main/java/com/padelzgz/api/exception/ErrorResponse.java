package com.padelzgz.api.exception;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorResponse {

    private int code;
    private String message;
    private Map<String, String> errors;

    private ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.errors = new HashMap<>();
    }

    private ErrorResponse(int code, String message, Map<String, String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse generalError(int code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse notFound(String message) {
        return new ErrorResponse(404, message);
    }

    public static ErrorResponse badRequest(String message) {
        return new ErrorResponse(400, message);
    }

    public static ErrorResponse conflict(String message) {
        return new ErrorResponse(409, message);
    }

    public static ErrorResponse validationError(Map<String, String> errors) {
        return new ErrorResponse(400, "Error de validación", errors);
    }

    public static ErrorResponse internalError() {
        return new ErrorResponse(500, "Error interno del servidor");
    }
}