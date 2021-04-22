package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;

/**
 * @author Pettyfer
 */
public interface IPersistentVolumeClaimService {

    void create(String namespace, PersistentVolumeClaim persistentVolumeClaim);

    Boolean update(String namespace, String name, PersistentVolumeClaim persistentVolumeClaim);

}
