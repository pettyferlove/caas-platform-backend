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
import java.util.ArrayList;
import java.util.List;

/**
 * Search
 *
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Search implements Serializable {
    private static final long serialVersionUID = 3765339603322263064L;

    public static final String URL = "/api/search";

    @JsonProperty("project")
    @Valid
    private List<Project> project = new ArrayList<>();

    @JsonProperty("chart")
    @Valid
    private List<SearchResult> chart = new ArrayList<>();

    @JsonProperty("repository")
    @Valid
    private List<SearchRepository> repository = new ArrayList<>();

}

