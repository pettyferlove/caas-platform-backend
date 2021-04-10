package com.github.pettyfer.caas.global.exception;


import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 自定义RuntimeException
 *
 * @author Petty
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseRuntimeException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 5785773026238677601L;
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public BaseRuntimeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaseRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
