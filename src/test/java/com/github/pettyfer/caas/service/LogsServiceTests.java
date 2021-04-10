package com.github.pettyfer.caas.service;

import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("compay")
public class LogsServiceTests {

    @Autowired
    private KubernetesClient kubernetesClient;

    @Test
    public void getLogs() {

        //获取最近20条日志记录
        // kubernetesClient.pods().inNamespace("jndjrd-qyd-prod").withName("jndjrd-qyd-core-59657c5cd5-6twdj").tailingLines(20).watchLog(System.out);

        // 获取全部日志
        String log = kubernetesClient.pods().inNamespace("jndjrd-qyd-prod").withName("mysql-2").inContainer("mysql").getLog();
        System.out.println(log);

    }

    @Test
    public void shell() throws InterruptedException {

        new ConfigMapBuilder()
                .build();
        kubernetesClient.configMaps().inNamespace("auto1").create();

        ExecWatch watch = kubernetesClient.pods().inNamespace("jndjrd-qyd-prod").withName("jndjrd-qyd-core-59657c5cd5-6twdj")
                .readingInput(System.in)
                .writingOutput(System.out)
                .writingError(System.err)
                .exec();

        Thread.sleep(60 * 1000);

    }

    private static class SimpleListener implements ExecListener {

        @Override
        public void onOpen(Response response) {
            System.out.println("The shell will remain open for 10 seconds.");
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            System.err.println("shell barfed");
        }

        @Override
        public void onClose(int code, String reason) {
            System.out.println("The shell will now close.");
        }
    }

}
