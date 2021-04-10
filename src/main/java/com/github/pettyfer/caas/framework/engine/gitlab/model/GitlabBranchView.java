package com.github.pettyfer.caas.framework.engine.gitlab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.gitlab.api.models.GitlabBranchCommit;

import java.io.Serializable;

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
public class GitlabBranchView implements Serializable {

    private static final long serialVersionUID = 5701175907144529696L;

    private String name;

    private GitlabBranchCommit commit;

    private boolean branchProtected;

}
