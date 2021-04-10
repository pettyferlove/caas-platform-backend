package com.github.pettyfer.caas.framework.engine.docker.register.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * SearchRepository
 * @author Petty
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryView implements Serializable {
  private static final long serialVersionUID = 1732428920064309110L;

  private String repositoryName;

  private String projectName;

  private Boolean projectPublic;

  private Integer tagsCount;

  private Integer projectId;

  private Integer pullCount;

  private String pullUrl;

}

