package com.takeitfree.auth.globalExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class RepresentationException {
    private String message;
}
