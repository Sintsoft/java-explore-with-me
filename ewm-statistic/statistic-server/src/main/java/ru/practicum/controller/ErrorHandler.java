package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.utility.dto.ErrorResponseDTO;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;
import ru.practicum.utility.exceptions.EwmSQLFailedException;
import ru.practicum.utility.exceptions.EwmValidationViolationException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleInvalidRequest(EwmInvalidRequestParameterException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidationViolation(EwmValidationViolationException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleNotFound(EwmEntityNotFoundException ex) {
        return new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleSQLfail(EwmSQLFailedException ex) {
        return new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Database interaction failed",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleMissingParan(MissingServletRequestParameterException ex) {
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleSQLfail(Exception ex) {
        return new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ex.getClass().toString(),
                ex.getMessage(),
                LocalDateTime.now());
    }
}
