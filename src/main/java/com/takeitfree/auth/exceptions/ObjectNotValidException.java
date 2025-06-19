package com.takeitfree.auth.exceptions;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class ObjectNotValidException extends RuntimeException {

    private Set<String> violations;

    public String getMessage() {
        return String.join("\n", violations);
    }

}
