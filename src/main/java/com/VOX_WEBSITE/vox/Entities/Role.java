package com.VOX_WEBSITE.vox.Entities;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    USER("USER"), ADMIN("ADMIN");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }
}
