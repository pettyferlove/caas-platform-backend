package com.github.pettyfer.caas.framework.core.event;


/**
 * @author Pettyfer
 */
public class GitlabPushEvent extends PushEvent {

    private static final long serialVersionUID = 1L;

    private String buildId;

    private GitlabPushDetails details;

    public GitlabPushEvent(Object source, String message, String buildId, GitlabPushDetails details) {
        super(source, message);
        this.buildId = buildId;
        this.details = details;
    }

    public String getAutoBuildId() {
        return buildId;
    }

    public void setAutoBuildId(String autoBuildId) {
        this.buildId = autoBuildId;
    }

    public GitlabPushDetails getDetails() {
        return details;
    }

    public void setDetails(GitlabPushDetails details) {
        this.details = details;
    }
}
