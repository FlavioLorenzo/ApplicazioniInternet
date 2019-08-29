package com.internet_application.backend.Enums;

/**
 * Represents the status of a ride as seen by the users
 * For more information on how to persist an enum refer to this article: https://www.baeldung.com/jpa-persisting-enums-in-jpa
 */
public enum ShiftStatus {
    NEW(1, "New"),
    CONFIRMED(2,"Confirmed"),
    VIEWED(3,"Viewed");

    private int code;
    private String description;

    ShiftStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
