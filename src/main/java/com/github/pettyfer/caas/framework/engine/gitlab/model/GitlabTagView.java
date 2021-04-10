package com.github.pettyfer.caas.framework.engine.gitlab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabRelease;

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
public class GitlabTagView implements Serializable {

    private static final long serialVersionUID = 815932475867283208L;
    private GitlabCommit commit;

    private GitlabRelease release;

    private String name;

    private String message;

}
