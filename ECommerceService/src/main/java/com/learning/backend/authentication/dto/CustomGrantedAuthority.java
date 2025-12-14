package com.learning.backend.authentication.dto;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
public class CustomGrantedAuthority implements GrantedAuthority {

    private String authority;
    @Override
    public String getAuthority() {
        if (authority != null && !authority.startsWith("ROLE_")) {
            return "ROLE_" + authority;
        }
        return authority;
    }
}
