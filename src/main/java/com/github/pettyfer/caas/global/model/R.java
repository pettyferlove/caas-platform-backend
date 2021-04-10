package com.github.pettyfer.caas.global.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应数据包装
 *
 * @param <T>
 * @author Petty
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;

    private long timestamp;

    private String message;

    private T data;

    public R() {
        super();
        this.timestamp = System.currentTimeMillis();
    }

    public R(T data) {
        super();
        this.data = data;
        this.status = 200;
        this.message = "success";
        this.timestamp = System.currentTimeMillis();
    }

    public R(T data, String message) {
        super();
        this.data = data;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public R(int status, String message, T data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public R(Throwable e) {
        super();
        this.status = 500;
        this.message = e.getMessage();
        this.timestamp = System.currentTimeMillis();
    }


}
