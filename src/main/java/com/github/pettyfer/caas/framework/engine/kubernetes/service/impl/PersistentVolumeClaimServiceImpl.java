package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IPersistentVolumeClaimService;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class PersistentVolumeClaimServiceImpl implements IPersistentVolumeClaimService {

    private final KubernetesClient kubernetesClient;

    public PersistentVolumeClaimServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public void create(String namespace, PersistentVolumeClaim persistentVolumeClaim) {
        kubernetesClient.persistentVolumeClaims().inNamespace(namespace).create(persistentVolumeClaim);
    }

    @Override
    public Boolean update(String namespace, String name, PersistentVolumeClaim persistentVolumeClaim) {
        kubernetesClient.persistentVolumeClaims().inNamespace(namespace).withName(name).delete();
        kubernetesClient.persistentVolumeClaims().inNamespace(namespace).create(persistentVolumeClaim);
        return true;
    }
}
