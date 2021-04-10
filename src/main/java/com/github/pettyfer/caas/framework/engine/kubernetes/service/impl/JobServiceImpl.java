package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IJobService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class JobServiceImpl implements IJobService {

    private final KubernetesClient kubernetesClient;

    public JobServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Job create(String namespace, Job job) {
        return kubernetesClient.batch().jobs().inNamespace(namespace).createOrReplace(job);
    }

    @Override
    public Job get(String namespace, String name) {
        return kubernetesClient.batch().jobs().inNamespace(namespace).withName(name).get();
    }

    @Override
    public Pod loadPod(String namespace, String jobId) {
        List<Pod> pods = kubernetesClient.pods().inNamespace(namespace).withLabel("job-name", jobId).list().getItems();
        if (pods.isEmpty()) {
            return null;
        } else {
            return pods.get(0);
        }
    }

    @Override
    public String log(String namespace, String podName, String containerName) {
        return kubernetesClient.pods().inNamespace(namespace).withName(podName).inContainer(containerName).tailingLines(2000).getLog();
    }

    @Override
    public void delete(String namespace, String jobId) {
        kubernetesClient.batch().jobs().inNamespace(namespace).withName(jobId).delete();
    }
}
