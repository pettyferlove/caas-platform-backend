package com.github.pettyfer.caas.framework.engine.docker.register.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Petty
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryTagView implements Serializable {

    private static final long serialVersionUID = 366337013000902142L;

    private String name;

    private String author;

    private String dockerVersion;

    private List<Label> labels;

    private String created;

    private String architecture;

    private Object signature;

    private String os;

    private String digest;

    private Integer size;

}
