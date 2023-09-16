package com.iamalexvybornyi.passportmanagementsystem.controller.exception;

import lombok.NonNull;

public class EntityNotFoundException extends RuntimeException {

    @NonNull
    public EntityNotFoundException() {
        super("Required entity is not found!");
    }
}
