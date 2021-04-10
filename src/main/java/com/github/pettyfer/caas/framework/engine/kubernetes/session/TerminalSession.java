package com.github.pettyfer.caas.framework.engine.kubernetes.session;

import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.Serializable;

/**
 * @author Pettyfer
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TerminalSession implements Serializable {

    private static final long serialVersionUID = -7328839423281671307L;

    private Session session;

    private ExecWatch execWatch;

    public void close() {
        try {
            this.session.close();
            this.execWatch.close();
        } catch (Exception e) {
            log.warn("TerminalSession clone waring!");
        }
    }

}
