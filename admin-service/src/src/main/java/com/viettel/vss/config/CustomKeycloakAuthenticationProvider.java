/*
package com.viettel.vss.config;

import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

*/
/**
 * @author truongbx7
 * @project vss-fw
 * @date 6/12/2023
 *//*

public class CustomKeycloakAuthenticationProvider extends KeycloakAuthenticationProvider {
    private GrantedAuthoritiesMapper grantedAuthoritiesMapper;

    public void setGrantedAuthoritiesMapper(GrantedAuthoritiesMapper grantedAuthoritiesMapper) {
        this.grantedAuthoritiesMapper = grantedAuthoritiesMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (String role : token.getAccount().getRoles()) {
            grantedAuthorities.add(new KeycloakRole(role));
        }


        return new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), mapAuthorities(grantedAuthorities));
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return grantedAuthoritiesMapper != null
                ? grantedAuthoritiesMapper.mapAuthorities(authorities)
                : authorities;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return KeycloakAuthenticationToken.class.isAssignableFrom(aClass);
    }


}*/
