package com.iamalexvybornyi.passportmanagementsystem.model.passport;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static Status getPassportStatusFromString(String value) {
        for (Status status : Status.values()) {
            if (status.toString().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
}
