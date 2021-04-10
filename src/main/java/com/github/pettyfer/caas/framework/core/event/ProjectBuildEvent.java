package com.github.pettyfer.caas.framework.core.event;

import com.github.pettyfer.caas.global.constants.BuildStatus;
import org.springframework.context.ApplicationEvent;

/**
 * @author Pettyfer
 */
public class ProjectBuildEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private String jobId;
    private BuildStatus status;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ProjectBuildEvent(Object source, String jobId, BuildStatus status) {
        super(source);
        this.jobId = jobId;
        this.status = status;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public BuildStatus getStatus() {
        return status;
    }

    public void setStatus(BuildStatus status) {
        this.status = status;
    }
}
