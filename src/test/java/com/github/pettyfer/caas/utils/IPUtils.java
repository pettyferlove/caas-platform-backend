package com.github.pettyfer.caas.utils;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

public class IPUtils {

    @Test
    @SneakyThrows
    public void testContent(){
        String b = "http://192.168.13.61";
        System.out.println(URLResolutionUtil.ip(b));;
        System.out.println(URLResolutionUtil.port(b));;
        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Local HostAddress:"+addr.getHostAddress());
        System.out.println("Local host name: "+addr.getHostName());
    }

}
