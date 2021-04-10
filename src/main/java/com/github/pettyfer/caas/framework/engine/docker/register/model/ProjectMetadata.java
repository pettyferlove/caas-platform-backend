package com.github.pettyfer.caas.framework.engine.docker.register.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * ProjectMetadata
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectMetadata  implements Serializable {
  private static final long serialVersionUID = 7872053442880557112L;
  
  @JsonProperty("enable_content_trust")
  private String enableContentTrust;

  @JsonProperty("auto_scan")
  private String autoScan;

  @JsonProperty("severity")
  private String severity;

  @JsonProperty("reuse_sys_cve_whitelist")
  private String reuseSysCveWhitelist;

  @JsonProperty("public")
  private String _public;

  @JsonProperty("prevent_vul")
  private String preventVul;
}

