package com.github.pettyfer.caas.framework.engine.docker.register.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * Repository
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository implements Serializable {
    private static final long serialVersionUID = 6183397123815107498L;

    public static final String URL_GET = "/api/repositories";

    @JsonProperty("update_time")
    private String updateTime = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("tags_count")
    private Integer tagsCount = null;

    @JsonProperty("labels")
    @Valid
    private List<Label> labels = null;

    @JsonProperty("creation_time")
    private String creationTime = null;

    @JsonProperty("star_count")
    private Integer starCount = null;

    @JsonProperty("project_id")
    private Integer projectId = null;

    @JsonProperty("pull_count")
    private Integer pullCount = null;

    @JsonProperty("id")
    private Integer id = null;

    @JsonProperty("name")
    private String name = null;
}

