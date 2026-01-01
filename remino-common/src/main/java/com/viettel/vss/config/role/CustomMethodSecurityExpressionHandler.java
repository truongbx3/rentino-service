package com.viettel.vss.config.role;

import com.viettel.vss.util.CommonProperty;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * @author truongbx7
 * @project vss-fw
 * @date 6/8/2023
 */
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    RoleConfig roleConfig;
    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(this.getPermissionEvaluator());
        root.setTrustResolver(this.getTrustResolver());
        root.setRoleHierarchy(this.getRoleHierarchy());
        root.setDefaultRolePrefix(this.getDefaultRolePrefix());
        root.setRoleConfig(roleConfig);
        return root;
    }
    public void setRoleConfig(RoleConfig roleConfig) {
        this.roleConfig = roleConfig;
    }
}
