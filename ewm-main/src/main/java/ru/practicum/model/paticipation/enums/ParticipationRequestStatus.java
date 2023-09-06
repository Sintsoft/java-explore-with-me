package ru.practicum.model.paticipation.enums;

public enum ParticipationRequestStatus {

    PENDING("PENDING"),

    CONFIRMED("CONFIRMED"),

    CANCELED("CANCELED"),

    REJECTED("REJECTED");

    private String code;

    ParticipationRequestStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
