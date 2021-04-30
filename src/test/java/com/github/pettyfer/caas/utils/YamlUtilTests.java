package com.github.pettyfer.caas.utils;

import io.fabric8.kubernetes.api.model.HTTPGetActionBuilder;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.ProbeBuilder;
import org.junit.jupiter.api.Test;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("dev")
public class YamlUtilTests {

    @Test
    public void test() {
        Probe probe = new ProbeBuilder().withHttpGet(new HTTPGetActionBuilder()
                .withNewPath("health")
                .withNewScheme("HTTP")
                .withNewPort(8885)
                .build())
                // 探针超时时间
                .withTimeoutSeconds(10)
                // 探针频率
                .withPeriodSeconds(10)
                // 延迟多少秒执行
                .withInitialDelaySeconds(30)
                .withSuccessThreshold(1)
                .withFailureThreshold(3)
                .build();
        String toYaml = YamlUtil.objToYaml(probe);
        System.out.println(toYaml);


    }

}
