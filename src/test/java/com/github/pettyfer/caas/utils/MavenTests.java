package com.github.pettyfer.caas.utils;

import lombok.SneakyThrows;
import org.apache.maven.shared.invoker.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;
import java.util.Properties;

public class MavenTests {

    @Test
    @SneakyThrows
    public void mavenPackageTest(){
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("D:/Maven"));
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("D:/CAAS/workspace/demo/pom.xml"));
        request.setGoals( Collections.singletonList( "package" ) );
        Properties properties = new Properties();
        properties.setProperty("skipTests", "true");
        request.setProperties(properties);
        invoker.execute(request);
    }
}
