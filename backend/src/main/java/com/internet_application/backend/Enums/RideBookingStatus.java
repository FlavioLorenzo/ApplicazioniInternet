package com.internet_application.backend.Enums;

/**
 * Represents the status of a ride as seen by the users
 * For more information on how to persist an enum refer to this article: https://www.baeldung.com/jpa-persisting-enums-in-jpa
 */
public enum RideBookingStatus {
    NOT_STARTED(1, "Not started yet"),
    IN_PROGRESS(2,"In Progress"),
    TERMINATED(3,"Terminated");

    private int code;
    private String description;

    RideBookingStatus(int code, String description) {
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
