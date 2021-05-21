package com.github.pettyfer.caas.global.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "kubernetes.app-center")
public class AppCenterProperties {

    /**
     * 应用中心地址
     */
    @Value("${kubernetes.app-center.url:http://127.0.0.1:30080}")
    private String url;

    /**
     * 访问Token
     */
    private String token;

}
