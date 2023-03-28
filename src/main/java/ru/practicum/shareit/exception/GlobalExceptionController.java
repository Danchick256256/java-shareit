package ru.practicum.shareit.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingUnknownStateException;
import ru.practicum.shareit.item.exception.ItemBadRequestException;
import ru.practicum.shareit.user.exception.UserNotUniqueEmailException;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotUniqueEmailException.class)
    public ResponseEntity<Object> errorResponse(UserNotUniqueEmailException e) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Object> errorResponse(EntityNotFoundException e) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Object> errorResponse(ItemBadRequestException e) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Object> errorResponse(BookingBadRequestException e) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Object> errorResponse(BookingUnknownStateException e) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}




