package com.github.pettyfer.caas.framework.core.message;

import com.github.pettyfer.caas.global.model.ServerMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pettyfer
 */
@Slf4j
@RestController
public class InitMessageWebSocket {

    @SubscribeMapping("/init/subscribe")
    public ServerMessage<String> subscribe() {
        return new ServerMessage<>("welcome!");
    }

}
