package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.utility.dto.ErrorResponseDTO;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;
import ru.practicum.utility.exceptions.EwmSQLFailedException;
import ru.practicum.utility.exceptions.EwmValidationViolationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleInvalidRequest(EwmInvalidRequestParameterException ex) {
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidationViolation(EwmValidationViolationException ex) {
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleNotFound(EwmEntityNotFoundException ex) {
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleSQLfail(EwmSQLFailedException ex) {
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleSQLfail(Exception ex) {
        return new ErrorResponseDTO(ex.getMessage() + " " + ex.getClass());
    }
}
