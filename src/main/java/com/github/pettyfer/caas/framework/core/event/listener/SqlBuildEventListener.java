package com.github.pettyfer.caas.framework.core.event.listener;

import com.github.pettyfer.caas.framework.core.event.SqlBuildEvent;
import com.github.pettyfer.caas.framework.core.service.ISqlBuildCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Pettyfer
 */
@Slf4j
@Component
public class SqlBuildEventListener implements ApplicationListener<SqlBuildEvent> {

    private final ISqlBuildCoreService sqlBuildCoreService;

    public SqlBuildEventListener(ISqlBuildCoreService sqlBuildCoreService) {
        this.sqlBuildCoreService = sqlBuildCoreService;
    }

    @Override
    public void onApplicationEvent(SqlBuildEvent buildEvent) {
        sqlBuildCoreService.updateStatus(buildEvent.getJobId(), buildEvent.getStatus());
    }
}
