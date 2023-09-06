package ru.practicum.utility.exceptions;

public class EwmSQLContraintViolation extends RuntimeException {

    public EwmSQLContraintViolation(String message) {
        super(message);
    }
}
