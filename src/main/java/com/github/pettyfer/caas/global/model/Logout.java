package com.github.pettyfer.caas.global.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Petty
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logout implements Serializable {
    private static final long serialVersionUID = 1171266392291021795L;
    private String message;
    private long timestamp;
    private int status;
}
