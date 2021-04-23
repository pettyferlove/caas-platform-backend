package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.Job;

/**
 * @author Pettyfer
 */
public interface IJobService {

    /**
     * 创建一个Job
     *
     * @param namespace 命名空间
     * @param job       任务
     * @return Job
     */
    Job create(String namespace, Job job);

    Job get(String namespace, String name);

    Pod loadPod(String namespace, String jobId);

    String log(String namespace, String podName, String containerName);

    void delete(String namespace, String jobId);
}
