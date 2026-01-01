package com.viettel.vss.config.common;


import com.viettel.vss.util.PropertyDecryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CrmEncryptedPropertyConfig extends PropertySourcesPlaceholderConfigurer {
    
    private ConfigurableEnvironment environment;
    
    private static final String ENCRYPTED_REGEX = "Encrypt\\{.*\\}";
    
    @Override
    public void setEnvironment(Environment environment) {
        super.setEnvironment(environment);
        this.environment = (ConfigurableEnvironment) environment;
    }
    
    @Override
    protected void loadProperties(Properties properties) {
        this.localOverride = true;
        
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource) {
                String[] propertyNames = ((EnumerablePropertySource) propertySource).getPropertyNames();
                for (String propertyName : propertyNames) {
                    String propertyValue = propertySource.getProperty(propertyName).toString();
                    String decryptedValue = "";
                    try {
                        decryptedValue = decryptProperty(propertyValue);
                    } catch (Exception e) {
                        log.error("Decrypt property value for: " + propertyName + " error. " +
                                "Error messages: " + e.getMessage());
                    }
                    
                    properties.setProperty(propertyName, decryptedValue);
                }
            }
        }
    }
    
    private String decryptProperty(String propertyValue) throws Exception {
        Pattern pattern = Pattern.compile(ENCRYPTED_REGEX);
        Matcher matcher = pattern.matcher(propertyValue);
        if (matcher.find()) {
            String encryptMatchedStr = propertyValue.substring(8, propertyValue.length() - 1);
            return PropertyDecryptionUtil.decrypt(encryptMatchedStr);
        }
        
        return propertyValue;
    }
    
}

