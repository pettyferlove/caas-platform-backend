package com.github.pettyfer.caas.framework.core.event.publisher;

import com.github.pettyfer.caas.framework.core.event.GitlabPushDetails;
import com.github.pettyfer.caas.framework.core.event.GitlabPushEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
public class GitlabPushEventPublisher {

    private final ApplicationContext context;

    public GitlabPushEventPublisher(ApplicationContext context) {
        this.context = context;
    }

    public void push(String buildId, GitlabPushDetails details) {
        context.publishEvent(new GitlabPushEvent(this, "push event", buildId, details));
    }

}
