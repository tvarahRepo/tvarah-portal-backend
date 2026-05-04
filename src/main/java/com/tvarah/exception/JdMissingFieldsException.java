package com.tvarah.exception;

import java.util.List;

public class JdMissingFieldsException extends RuntimeException {

    private final List<String> missingFields;

    public JdMissingFieldsException(List<String> missingFields) {
        super("JD is missing required fields: " + String.join(", ", missingFields));
        this.missingFields = missingFields;
    }

    public List<String> getMissingFields() {
        return missingFields;
    }
}
