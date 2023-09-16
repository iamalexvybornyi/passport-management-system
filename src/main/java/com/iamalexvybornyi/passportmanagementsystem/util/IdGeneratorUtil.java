package com.iamalexvybornyi.passportmanagementsystem.util;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorUtil {

    @NonNull
    public String generatePersonId() {
        return UUID.randomUUID().toString();
    }

    @NonNull
    public String generatePassportId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
