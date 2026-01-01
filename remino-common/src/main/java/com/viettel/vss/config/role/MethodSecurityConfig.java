package com.viettel.vss.config.role;

import com.viettel.vss.util.CommonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * @author truongbx7
 * @project vss-fw
 * @date 6/8/2023
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Autowired
    RoleConfig roleConfig ;
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomMethodSecurityExpressionHandler handler = new CustomMethodSecurityExpressionHandler();
        handler.setRoleConfig(roleConfig);
        return handler;
    }
}
