package com.github.pettyfer.caas.framework.core.event.publisher;

import com.github.pettyfer.caas.framework.core.event.ProjectBuildEvent;
import com.github.pettyfer.caas.global.constants.BuildStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Pettyfer
 */
@Slf4j
@Component
public class ProjectBuildEventPublisher {

    private final ApplicationContext context;

    public ProjectBuildEventPublisher(ApplicationContext context) {
        this.context = context;
    }

    public void push(String jobId, BuildStatus status) {
        context.publishEvent(new ProjectBuildEvent(this, jobId, status));
    }

}
