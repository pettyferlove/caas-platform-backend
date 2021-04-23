package com.github.pettyfer.caas.framework.core.event.publisher;

import com.github.pettyfer.caas.framework.core.event.BuildEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Pettyfer
 */
@Slf4j
@Component
public class BuildEventPublisher {

    private final ApplicationContext context;

    public BuildEventPublisher(ApplicationContext context) {
        this.context = context;
    }

    public void push(String projectBuildId, String imageName, String tag) {
        context.publishEvent(new BuildEvent(this, projectBuildId, imageName, tag));
    }

}
