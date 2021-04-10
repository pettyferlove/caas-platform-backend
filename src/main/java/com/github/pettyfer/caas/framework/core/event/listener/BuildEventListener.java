package com.github.pettyfer.caas.framework.core.event.listener;

import com.github.pettyfer.caas.framework.core.service.IApplicationDeploymentCoreService;
import com.github.pettyfer.caas.framework.core.event.BuildEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Pettyfer
 */
@Slf4j
@Component
public class BuildEventListener implements ApplicationListener<BuildEvent> {

    private final IApplicationDeploymentCoreService applicationDeploymentCoreService;

    public BuildEventListener(IApplicationDeploymentCoreService applicationDeploymentCoreService) {
        this.applicationDeploymentCoreService = applicationDeploymentCoreService;
    }

    @Override
    public void onApplicationEvent(BuildEvent buildEvent) {
        applicationDeploymentCoreService.autoDeployment(buildEvent.getAutoBuildId(), buildEvent.getImageName(), buildEvent.getTag());
    }
}
