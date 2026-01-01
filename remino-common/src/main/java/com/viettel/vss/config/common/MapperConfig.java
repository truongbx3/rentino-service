package com.viettel.vss.config.common;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author truongbx7
 */
@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper getMapper() {
        var mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }
}
