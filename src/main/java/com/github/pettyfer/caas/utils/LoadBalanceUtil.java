package com.github.pettyfer.caas.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Random;

/**
 * @author Pettyfer
 */
@UtilityClass
public class LoadBalanceUtil {

    @SneakyThrows
    public String chooseServer(String config) {
        String[] servers = config.split(";");
        if (servers.length < 1) {
            throw new Exception("server config error");
        }
        Random random = new Random();
        int pos = random.nextInt(servers.length);
        return servers[pos];
    }

}
