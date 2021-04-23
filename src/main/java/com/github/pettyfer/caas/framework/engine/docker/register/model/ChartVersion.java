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
 * A specified chart entry
 *
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChartVersion implements Serializable {
    private static final long serialVersionUID = 8292531189794676567L;
    @JsonProperty("engine")
    private String engine = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("deprecated")
    private Boolean deprecated = null;

    @JsonProperty("appVersion")
    private String appVersion = null;

    @JsonProperty("apiVersion")
    private String apiVersion = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("sources")
    @Valid
    private List<String> sources = null;

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("keywords")
    @Valid
    private List<String> keywords = null;

    @JsonProperty("home")
    private String home = null;

    @JsonProperty("icon")
    private String icon = null;

    @JsonProperty("labels")
    private Labels labels = null;

    @JsonProperty("removed")
    private Boolean removed = null;

    @JsonProperty("digest")
    private String digest = null;

    @JsonProperty("urls")
    @Valid
    private List<String> urls = null;

    @JsonProperty("created")
    private String created = null;


}

