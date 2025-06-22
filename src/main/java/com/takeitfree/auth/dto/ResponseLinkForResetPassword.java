package com.takeitfree.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
public class ResponseLinkForResetPassword implements Serializable {

    private String message;
}
