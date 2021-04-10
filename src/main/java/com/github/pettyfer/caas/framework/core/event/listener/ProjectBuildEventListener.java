package com.github.pettyfer.caas.framework.core.event.listener;

import com.github.pettyfer.caas.framework.core.service.IProjectBuildCoreService;
import com.github.pettyfer.caas.framework.core.event.ProjectBuildEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Pettyfer
 */
@Slf4j
@Component
public class ProjectBuildEventListener implements ApplicationListener<ProjectBuildEvent> {

    private final IProjectBuildCoreService projectBuildCoreService;

    public ProjectBuildEventListener(IProjectBuildCoreService projectBuildCoreService) {
        this.projectBuildCoreService = projectBuildCoreService;
    }

    @Override
    public void onApplicationEvent(ProjectBuildEvent buildEvent) {
        projectBuildCoreService.updateStatus(buildEvent.getJobId(), buildEvent.getStatus());
    }
}
