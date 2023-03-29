package ru.practicum.shareit.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {
    @JsonProperty(value = "status")
    private final HttpStatus status;
    @JsonProperty(value = "error")
    private final String message;

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
}