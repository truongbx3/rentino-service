package com.viettel.vss.config;

import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

//    @Bean(destroyMethod = "stop")
//    public LocalOfficeManager officeManager() throws Exception {
//        LocalOfficeManager manager = LocalOfficeManager.builder()
//                // .officeHome("C:\\Program Files\\LibreOffice") // Windows nếu cần
//                .install()
//                .build();
//        manager.start();
//        return manager;
//    }
}
