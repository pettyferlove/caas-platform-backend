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
 * DetailedTag
 *
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailedTag implements Serializable {
    private static final long serialVersionUID = -4350081508337348227L;

    public static final String URL = "/api/repositories/{0}/tags";

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("author")
    private String author = null;

    @JsonProperty("docker_version")
    private String dockerVersion = null;

    @JsonProperty("labels")
    @Valid
    private List<Label> labels = null;

    @JsonProperty("created")
    private String created = null;

    @JsonProperty("architecture")
    private String architecture = null;

    @JsonProperty("signature")
    private Object signature = null;

    @JsonProperty("os")
    private String os = null;

    @JsonProperty("digest")
    private String digest = null;

    @JsonProperty("size")
    private Integer size = null;

}

