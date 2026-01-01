package com.viettel.vss.config;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.viettel.vss.config.bean.UserCustom;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class CustomFilter implements Filter {

    public static final String BEARER = "Bearer ";
    public static final String PERMISSIONS = "permissions";

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
//        String token = httpRequest.getHeader("Authorization")==null?"system":httpRequest.getHeader("Authorization");
//        String username = httpRequest.getHeader("uname")==null?"system":httpRequest.getHeader("uname");
//
//        this.grantedAuthority(username, token);
//        filterChain.doFilter(servletRequest, servletResponse);
//
//
//
//    }
@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String token = httpRequest.getHeader("Authorization");
        String username;

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // remove "Bearer "
            DecodedJWT jwt = JWT.decode(token);
            username = jwt.getClaim("sub").asString(); // lấy username từ claim sub
        } else {
            username = "system"; // fallback nếu không có token
        }
        this.grantedAuthority(username, token);
        filterChain.doFilter(servletRequest, servletResponse);
}


    public void grantedAuthority(String username, String token) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new UserCustom(username, "", true, true, true, true, new ArrayList(), token), "", (Collection)this.getRole(token).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public List<String> getRole(String token) {
        List<String> roles = new ArrayList();
        if (!ObjectUtils.isEmpty(token) && !Objects.equals(token, "system")) {
            token = token.replace(BEARER, "");
            DecodedJWT jwt = JWT.decode(token);
            roles.addAll(jwt.getClaim(PERMISSIONS).asList(String.class));
        }
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_OTHER");
        return roles;
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}
