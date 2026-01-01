package com.viettel.vss.config.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.viettel.vss.config.bean.UserCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountInfoFilter implements Filter {
    private final static Logger LOG = LoggerFactory.getLogger(AccountInfoFilter.class);
    public static final String SWAGGER = "swagger";
    public static final String API_DOC = "api-doc";
    public static final String AUTHORIZATION = "authorization";
    public static final String BEARER = "Bearer ";
    public static final String PREFERRED_USERNAME = "preferred_username";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        LOG.info("Starting Transaction for req :{}", req.getHeaderNames());
        Enumeration<String> headerNames = req.getHeaderNames();
        String username = "";
        String token = "";
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String element = headerNames.nextElement();
                LOG.debug("Header: {} : {}", element, req.getHeader(element));
                if (AUTHORIZATION.equalsIgnoreCase(element)) {
                    token = req.getHeader(element).replace(BEARER, "");
                }
            }
            if (StringUtils.hasLength(token)) {
                DecodedJWT jwt = JWT.decode(token);
                username = jwt.getClaims().get(PREFERRED_USERNAME).asString();
            }

        }
        // TODO: 7/1/2022  add api validate user here
        if (req.getRequestURI().contains(SWAGGER) || req.getRequestURI().contains(API_DOC)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // TODO: 7/1/2022  call admin api to get roles , user infor
            grantedAuthority(username, token);
            filterChain.doFilter(servletRequest, servletResponse);
            LOG.info("Committing Transaction for req :{}", req.getRequestURI());
        }
    }


    public void grantedAuthority(String username, String token) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                new UserCustom(username, "", true, true, true, true, new ArrayList<>(), token),
                "",
                getRole(username).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public List<String> getRole(String roleKey) {
// TODO:       get role base on role key
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_OTHER");
        return roles;
    }

}

