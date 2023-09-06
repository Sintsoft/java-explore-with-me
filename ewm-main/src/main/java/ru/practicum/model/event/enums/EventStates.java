package ru.practicum.model.event.enums;

public enum EventStates {

    PENDING("PENDING"),
    PUBLISHED("PUBLISHED"),
    CANCELED("CANCELED");

    private String code;

    EventStates(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}