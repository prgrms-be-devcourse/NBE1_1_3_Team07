package com.develetter.develetter.user.global.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * OAuth2User 커스텀을 위한 핸들
 */
@NoArgsConstructor
@AllArgsConstructor
public class CustomOAuthUser implements OAuth2User {

    private String accountId;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.accountId;
    }
}

