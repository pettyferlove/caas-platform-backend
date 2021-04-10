package com.github.pettyfer.caas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Petty
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@SpringBootApplication
public class CaaSPlatformApplication {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(CaaSPlatformApplication.class, args);
    }

}
