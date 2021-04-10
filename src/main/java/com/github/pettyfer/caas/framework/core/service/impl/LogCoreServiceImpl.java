package com.github.pettyfer.caas.framework.core.service.impl;

import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IJobService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.framework.core.service.ILogCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class LogCoreServiceImpl implements ILogCoreService {

    private final IBizNamespaceService bizNamespaceService;

    private final IJobService jobService;

    public LogCoreServiceImpl(IBizNamespaceService bizNamespaceService, IJobService jobService) {
        this.bizNamespaceService = bizNamespaceService;
        this.jobService = jobService;
    }

    @Override
    public String log(String namespaceId, String podName, String containerName) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            try {
                return jobService.log(namespaceOptional.get().getName(), podName, containerName);
            } catch (Exception e) {
                return "没有日志输出";
            }

        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }
}
