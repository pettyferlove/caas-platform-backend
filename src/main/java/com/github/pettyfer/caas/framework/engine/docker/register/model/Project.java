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
 * Project
 *
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project implements Serializable {

    public static final String URL = "/api/projects";

    private static final long serialVersionUID = 9095506816132413845L;
    @JsonProperty("update_time")
    private String updateTime = null;

    @JsonProperty("owner_name")
    private String ownerName = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("deleted")
    private Boolean deleted = null;

    @JsonProperty("owner_id")
    private Integer ownerId = null;

    @JsonProperty("repo_count")
    private Integer repoCount = null;

    @JsonProperty("creation_time")
    private String creationTime = null;

    @JsonProperty("togglable")
    private Boolean togglable = null;

    @JsonProperty("project_id")
    private Integer projectId = null;

    @JsonProperty("current_user_role_id")
    private Integer currentUserRoleId = null;

    @JsonProperty("current_user_role_ids")
    @Valid
    private List<Integer> currentUserRoleIds = null;

    @JsonProperty("chart_count")
    private Integer chartCount = null;

}

