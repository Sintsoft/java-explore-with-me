package ru.practicum.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.utility.dto.ErrorResponseDTO;
import ru.practicum.utility.exceptions.*;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionResponseController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleInvalidRequest(EwmInvalidRequestParameterException ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidationViolation(EwmValidationViolationException ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleSQLConstraintViolation(EwmSQLContraintViolation ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleNotFound(EwmEntityNotFoundException ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleConflict(EwmRequestParameterConflict ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO nadleMissingRequestParam(MissingServletRequestParameterException ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Database interaction failed",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO nadleSpringValidationFail(MethodArgumentNotValidException ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Validation failed",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleSQLConstraintConflict(DataIntegrityViolationException ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Validation failed",
                ex.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleSQLfail(EwmSQLFailedException ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Database interaction failed",
                ex.getMessage(),
                LocalDateTime.now());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleSQLfail(Exception ex) {
        logException(ex);
        return new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ex.getClass().toString(),
                ex.getMessage(),
                LocalDateTime.now());
    }

    private void logException(Throwable ex) {
        log.info("Throw of " + ex.getClass() + ", message: " + ex.getMessage());
    }
}
