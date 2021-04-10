package com.github.pettyfer.caas.framework.engine.docker.register.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ProjectReq
 *
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectCreate implements Serializable {

    public static final String URL = "/api/projects";

    private static final long serialVersionUID = 5670501538067705758L;
    @JsonProperty("count_limit")
    private Long countLimit = null;

    @JsonProperty("project_name")
    private String projectName = null;

    @JsonProperty("storage_limit")
    private Long storageLimit = null;

    @JsonProperty("metadata")
    private ProjectMetadata metadata = null;

}

