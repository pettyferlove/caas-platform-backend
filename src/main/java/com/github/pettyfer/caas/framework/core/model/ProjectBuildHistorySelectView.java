package com.github.pettyfer.caas.framework.core.model;

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
public class ProjectBuildHistorySelectView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileId;

    private String buildStatus;

    private String createTime;

}
