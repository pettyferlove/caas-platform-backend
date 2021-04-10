package com.github.pettyfer.caas.framework.engine.kubernetes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListQueryParams implements Serializable {

    private static final long serialVersionUID = 7816186026202535090L;
    /**
     * 分页页码
     */
    private int currentPage = 1;

    /**
     * 分页大小
     */
    private int pageSize = 10;


}
