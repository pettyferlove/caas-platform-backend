package com.github.pettyfer.caas.framework.core.event;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Pettyfer
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GitlabPushDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("user_name")
    private String username;

    @JsonProperty("user_username")
    private String userUsername;

    @JsonProperty("user_avatar")
    private String userAvatar;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("project_id")
    private String projectId;

    private String ref;

    private String message;

    @JsonProperty("checkout_sha")
    private String checkoutSha;

    private String before;

    private String after;

    @JsonProperty("commit")
    private List<CommitInfo> commits;

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    static class CommitUser implements Serializable {
        private static final long serialVersionUID = 1806719631520854601L;

        private String name;

        private String email;
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    static class CommitInfo implements Serializable {
        private static final long serialVersionUID = 7837566542351749379L;

        private String id;

        private String message;

        private CommitUser author;
    }

}
