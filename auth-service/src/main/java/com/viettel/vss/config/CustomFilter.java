package com.viettel.vss.config;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.viettel.vss.config.bean.UserCustom;
import com.viettel.vss.util.DataUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String token = httpRequest.getHeader("Authorization")==null?"system":httpRequest.getHeader("Authorization");
        String username = httpRequest.getHeader("uname")==null?"system":httpRequest.getHeader("uname");

        this.grantedAuthority(username, token);
        filterChain.doFilter(servletRequest, servletResponse);



    }
    public void grantedAuthority(String username, String token) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new UserCustom(username, "", true, true, true, true, new ArrayList(), token), "", (Collection)this.getRole(username).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public List<String> getRole(String roleKey) {
        List<String> roles = new ArrayList();
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
