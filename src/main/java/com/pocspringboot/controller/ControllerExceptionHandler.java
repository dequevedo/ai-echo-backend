package com.pocspringboot.controller;

import com.pocspringboot.exception.NotFoundException;
import com.pocspringboot.exception.UnprocessableEntityException;
import com.pocspringboot.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(Exception ex) {
        log.error("NotFoundException: ", ex);
        String code = "404.000";

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(code)
                .message(getMessage(code))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponse> unprocessableEntityExceptionHandler(Exception ex) {
        log.error("UnprocessableEntityException: ", ex);
        String code = "422.000";

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(code)
                .message(getMessage(code))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex) {
        log.error("Exception: ", ex);
        String code = "500.000";

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(code)
                .message(getMessage(code))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Exception: ", ex);
        String code = "400.001";

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(code)
                .message(getMessage(code))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String getMessage(String code, Object... args) {
        return this.messageSource.getMessage(code, args, Locale.getDefault());
    }
}
