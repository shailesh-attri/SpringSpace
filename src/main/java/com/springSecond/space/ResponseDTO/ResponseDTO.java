package com.springSecond.space.ResponseDTO;  // Updated package declaration

import org.springframework.http.HttpStatus;

public class ResponseDTO<T> {

    private HttpStatus status;
    private String message;
    private T result;
    private String error;

    // Constructors
    public ResponseDTO() {}

    public ResponseDTO(HttpStatus status, String message, T result, String error) {
        this.status = status;
        this.message = message;
        this.result = result;
        this.error = error;
    }

    // Success response
    public static <T> ResponseDTO<T> success(T result, String message) {
        return new ResponseDTO<>(HttpStatus.OK, message, result, null);
    }

    // Error response
    public static <T> ResponseDTO<T> error(HttpStatus status, String message, String error) {
        return new ResponseDTO<>(status, message, null, error);
    }

    // Getters and Setters
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", error='" + error + '\'' +
                '}';
    }
}
