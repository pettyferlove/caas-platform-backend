package com.github.pettyfer.caas.framework.core.event.publisher;

import com.github.pettyfer.caas.global.constants.BuildStatus;
import com.github.pettyfer.caas.framework.core.event.SqlBuildEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Pettyfer
 */
@Slf4j
@Component
public class SqlBuildEventPublisher {

    private final ApplicationContext context;

    public SqlBuildEventPublisher(ApplicationContext context) {
        this.context = context;
    }

    public void push(String jobId, BuildStatus status) {
        context.publishEvent(new SqlBuildEvent(this, jobId, status));
    }

}
