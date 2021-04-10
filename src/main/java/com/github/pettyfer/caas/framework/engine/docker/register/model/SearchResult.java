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
 * The chart search result item
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult implements Serializable {
    private static final long serialVersionUID = 9177710991686290638L;
    @JsonProperty("score")
    private Integer score = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("chart")
    private ChartVersion chart = null;

}

