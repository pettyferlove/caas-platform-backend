package com.github.pettyfer.caas.framework.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("ALL")
public class FileProcessResult implements Serializable {
    private static final long serialVersionUID = -3962764872427466393L;

    private String fileId;

    private String fileName;

    private int storeType;

    private String url;

    private String path;

    private String md5;

}
