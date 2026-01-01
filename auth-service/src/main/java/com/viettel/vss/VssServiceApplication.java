package com.viettel.vss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.*"})
@EntityScan("com.viettel.vss.entity")
public class VssServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VssServiceApplication.class, args);
    }
}
