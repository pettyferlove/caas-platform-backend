package com.github.pettyfer.caas.global.exception;


import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 自定义Exception
 *
 * @author Petty
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends Exception implements Serializable {
    private static final long serialVersionUID = -8390390631837103313L;
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
