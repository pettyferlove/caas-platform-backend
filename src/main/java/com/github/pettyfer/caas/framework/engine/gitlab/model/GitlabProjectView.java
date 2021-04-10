package com.github.pettyfer.caas.framework.engine.gitlab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.gitlab.api.models.GitlabUser;

import java.io.Serializable;
import java.util.Date;

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
public class GitlabProjectView implements Serializable {

    private static final long serialVersionUID = 2241246416054735020L;
    private Integer id;

    private String name;

    private String nameWithNamespace;

    private GitlabUser owner;

    private String description;

    private String defaultBranch;

    private String path;

    private String pathWithNamespace;

    private Date createdAt;

    private String sshUrl;

    private String webUrl;

    private String httpUrl;

    private Date lastActivityAt;

    private Boolean archived;


}
