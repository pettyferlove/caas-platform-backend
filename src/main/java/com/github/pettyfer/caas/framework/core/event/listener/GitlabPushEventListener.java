package com.github.pettyfer.caas.framework.core.event.listener;

import com.github.pettyfer.caas.framework.core.event.GitlabPushEvent;
import com.github.pettyfer.caas.framework.core.service.IProjectBuildCoreService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Pettyfer
 */
@Component
public class GitlabPushEventListener implements ApplicationListener<GitlabPushEvent> {

    private final IProjectBuildCoreService autoBuildCoreService;

    public GitlabPushEventListener(IProjectBuildCoreService autoBuildCoreService) {
        this.autoBuildCoreService = autoBuildCoreService;
    }

    /**
     * 转发至持续继承核心方法并开始构建
     *
     * @param gitlabPushEvent Gitlab Push事件
     */
    @Override
    public void onApplicationEvent(GitlabPushEvent gitlabPushEvent) {
        autoBuildCoreService.autoBuild(gitlabPushEvent.getAutoBuildId(), gitlabPushEvent.getDetails());
    }
}
