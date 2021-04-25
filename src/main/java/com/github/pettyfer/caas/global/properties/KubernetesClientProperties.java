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
@ConfigurationProperties(prefix = "kubernetes.client")
public class KubernetesClientProperties {

    /**
     * 主节点地址
     */
    @Value("${kubernetes.client.master-url:http://127.0.0.1:8001}")
    private String masterUrl;

    /**
     * Kubernetes用户Token
     */
    private String token;

    /**
     * CA证书（Base64）
     */
    private String certData;

}
