package com.viettel.vss.config.role;

import com.viettel.vss.util.CommonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

/**
 * @author truongbx7
 * @project vss-fw
 * @date 6/12/2023
 */
@Getter
@Setter
@Configuration
@PropertySources({
        @PropertySource(value = "classpath:role.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:role.yml", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:role-${spring.profiles.active}.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:role-${spring.profiles.active}.yml", ignoreResourceNotFound = true)})
public class RoleConfig {
    @Autowired
    private Environment env;

    @Autowired
    private CommonProperty commonProperty;

    @Value("${vss.role.admin-role:#{null}}")
    private String adminRole;

    @Value("${vss.role.authorization:#{null}}")
    private Boolean usingAuthorization;

    public String getValue(String key) {
        return env.getProperty(key);
    }

    public String getValue(String key, String defaulValue) {
        return env.getProperty(key, defaulValue);
    }

    public String getAdminRole() {
        if (commonProperty.getAdminRole()!= null && !commonProperty.getAdminRole().isEmpty()){
            return commonProperty.getAdminRole();
        }
        return adminRole;
    }

    public boolean isUsingAuthorization() {
        if (commonProperty.getUsingAuthorization()!= null){
            return commonProperty.getUsingAuthorization();
        }
        if (usingAuthorization!= null){
            return usingAuthorization;
        }
        return true;
    }
}
