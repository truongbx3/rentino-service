package com.viettel.vss.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

/**
 * @author truongbx
 * @date 2/14/2022
 */
@Getter
@Setter
@Component
@PropertySources({
		@PropertySource(value = "classpath:common.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:common.yml", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:common-${spring.profiles.active}.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:common-${spring.profiles.active}.yml", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:application-${spring.profiles.active}.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:application-${spring.profiles.active}.yml", ignoreResourceNotFound = true)})
public class CommonProperty {

	@Value("${vss.role.admin-role:#{null}}")
	private String adminRole;

	@Value("${vss.role.authorization:#{null}}")
	private Boolean usingAuthorization;

	@Value("${minio.endPoint}")
	private String endPoint;
	@Value("${minio.accessKey}")
	private String accessKey;
	@Value("${minio.secretKey}")
	private String secretKey;
}
