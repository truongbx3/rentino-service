package com.viettel.vss.config.role;

import com.viettel.vss.constant.CommonConstants;
import com.viettel.vss.util.StringUtils;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author truongbx7
 * @project vss-fw
 * @date 6/8/2023
 */
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private Object target;
    RoleConfig roleConfig;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    /**
     * create role dynamic base on role name and request mapping on controller
     *
     * @param request  servlet request
     * @param roleName name of role
     * @return access or not
     */
    public boolean autoRole(HttpServletRequest request, String... roleName) {
        if (request == null || roleName == null) {
            return false;
        }
        return checkRole(TypeRole.AUTO, request, roleName);

    }

    /**
     * check role base on fix role.
     * equivalent anyRole
     * @param roleName name of role
     * @return access or not
     */
    public boolean fixRole(String... roleName) {
        if (roleName == null){
            return false;
        }
        return checkRole(TypeRole.File,null,roleName);
    }
    /**
     * check role base on fix role.
     * equivalent anyRole
     * @param request key of role in file role.properties
     * @return access or not
     */
    public boolean fileRole(HttpServletRequest request) {
        if (request == null){
            return false;
        }
        return checkRole(TypeRole.File,request,null);
    }

    private boolean checkBaseRole(HttpServletRequest request, String... roleName) {
        String fullPath = request.getRequestURI().substring(request.getContextPath().length());
        String[] path = fullPath.split("/");
        String controllerName = fullPath.split("/")[1];

        if (path != null && path.length >= 1) {
            controllerName = path[1];
        }
        Set<String> setRole = new HashSet<>();

        for (String role : roleName) {
            setRole.add(Stream.of(CommonConstants.ROLE, controllerName.toUpperCase(), role)
                    .filter(s -> s != null && !s.isEmpty())
                    .collect(Collectors.joining(CommonConstants.SPECIAL_CHARACTER.UNDERLINED)));
        }
        if (!StringUtils.isNullOrEmpty(roleConfig.getAdminRole())) {
            setRole.add(roleConfig.getAdminRole());
        }
        String[] roles = Arrays.copyOf(setRole.toArray(), setRole.size(), String[].class);
        return this.hasAnyRole(roles);
    }

    private boolean checkRole(TypeRole typeRole, HttpServletRequest request, String... roleName) {
        if (!roleConfig.isUsingAuthorization()) {
            return true;
        }
        boolean status;
        switch (typeRole) {
            case AUTO:
                status = checkBaseRole(request, roleName);
                break;
            case File:
                status = checkFileRole(request);
                break;
            case FIX:
                status = this.hasAnyRole(roleName);
                break;
            default:
                status = false;
                break;
        }
        return status;
    }


    public boolean checkFileRole(HttpServletRequest request) {
        if (!roleConfig.isUsingAuthorization()) {
            return true;
        }

        String fullPath = request.getRequestURI().replace("/", ".");
        if (fullPath.startsWith(".")) {
            fullPath = fullPath.replaceFirst(".", "");
        }
        Set<String> setRole = new HashSet<>();
        String lstRole = roleConfig.getValue(fullPath);
        if (lstRole != null) {
            setRole = Arrays.stream(lstRole.split(",")).map(String::trim).filter(s -> s != null && !s.isEmpty()).collect(Collectors.toSet());
        }

        if (!StringUtils.isNullOrEmpty(roleConfig.getAdminRole())) {
            setRole.add(roleConfig.getAdminRole());
        }
        String[] roles = Arrays.copyOf(setRole.toArray(), setRole.size(), String[].class);
        return this.hasAnyRole(roles);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    public RoleConfig getRoleConfig() {
        return roleConfig;
    }

    public void setRoleConfig(RoleConfig roleConfig) {
        this.roleConfig = roleConfig;
    }
}
