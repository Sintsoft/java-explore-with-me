package ru.practicum.model.event.enums;

public enum EventSorts {

    EVENT_DATE("eventDate"),

    LIKES("likes"),
    VIEWS("VIEWS");

    private String code;

    EventSorts(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}