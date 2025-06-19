package com.takeitfree.auth.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class AttributeRoleRequest implements Serializable {

    private Long idUser;
    private Long idRole;
}
