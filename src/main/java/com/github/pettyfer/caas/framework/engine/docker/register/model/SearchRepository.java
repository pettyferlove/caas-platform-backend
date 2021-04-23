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
 * SearchRepository
 *
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRepository implements Serializable {
    private static final long serialVersionUID = 1732428920064309110L;
    @JsonProperty("repository_name")
    private String repositoryName = null;

    @JsonProperty("project_name")
    private String projectName = null;

    @JsonProperty("project_public")
    private Boolean projectPublic = null;

    @JsonProperty("tags_count")
    private Integer tagsCount = null;

    @JsonProperty("project_id")
    private Integer projectId = null;

    @JsonProperty("pull_count")
    private Integer pullCount = null;


}

