package com.github.pettyfer.caas.configuration;

import com.github.pettyfer.caas.global.properties.KubernetesClientProperties;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

/**
 * Kubernetes Client实例自动化配置
 *
 * @author Petty
 */
@Configuration
@ConditionalOnWebApplication
public class KubernetesClientAutoConfiguration {

    private final KubernetesClientProperties properties;

    public KubernetesClientAutoConfiguration(KubernetesClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    @SneakyThrows
    @Profile("dev-windows")
    KubernetesClient kubernetesClientForWindows() {
        Config config = new ConfigBuilder()
                .withNewMasterUrl(properties.getMasterUrl())
                .withNewOauthToken(properties.getToken())
                .build();
        return new DefaultKubernetesClient(config);
    }

    @Bean
    @SneakyThrows
    @Profile("dev-ubuntu")
    KubernetesClient kubernetesClientForUbuntu() {
        ClassPathResource resource = new ClassPathResource(properties.getCert());
        Config config = new ConfigBuilder()
                .withNewMasterUrl(properties.getMasterUrl())
                .withNewOauthToken(properties.getToken())
                .withNewCaCertData(FileCopyUtils.copyToByteArray(resource.getInputStream()))
                .build();
        return new DefaultKubernetesClient(config);
    }

    @Bean
    @SneakyThrows
    @Profile("dev-centos")
    KubernetesClient kubernetesClientForCentos() {
        ClassPathResource resource = new ClassPathResource(properties.getCert());
        Config config = new ConfigBuilder()
                .withNewMasterUrl(properties.getMasterUrl())
                .withNewOauthToken(properties.getToken())
                .withNewCaCertData(FileCopyUtils.copyToByteArray(resource.getInputStream()))
                .build();
        return new DefaultKubernetesClient(config);
    }

}
