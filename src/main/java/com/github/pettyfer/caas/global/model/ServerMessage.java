package com.github.pettyfer.caas.global.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerMessage<T> implements Serializable {
    private static final long serialVersionUID = -4144501047469456841L;

    private int status = 200000;

    private long timestamp;

    private T content;

    public ServerMessage(T message) {
        this.timestamp = System.currentTimeMillis();
        this.content = message;
    }

    public ServerMessage(int status, T content) {
        this.status = status;
        this.content = content;
    }
}
