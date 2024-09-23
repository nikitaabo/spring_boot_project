package com.example.spring.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Status implements GrantedAuthority {

    PENDING,
    COMPLETED,
    DELIVERED;

    @Override
    public String getAuthority() {
        return name();
    }
}
