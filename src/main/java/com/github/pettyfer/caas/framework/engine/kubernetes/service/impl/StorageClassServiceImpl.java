package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.StorageClassView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IStorageClassService;
import com.github.pettyfer.caas.global.model.Page;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class StorageClassServiceImpl implements IStorageClassService {

    private final KubernetesClient kubernetesClient;

    public StorageClassServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public List<StorageClassView> list() {
        List<StorageClass> storageClasses = kubernetesClient.storage().storageClasses().list().getItems();
        return storageClasses.stream().map(i -> {
            StorageClassView storageClassView = new StorageClassView();
            storageClassView.setName(i.getMetadata().getName());
            storageClassView.setProvisioner(i.getProvisioner());
            storageClassView.setReclaimPolicy(i.getReclaimPolicy());
            storageClassView.setCreationTimestamp(LocalDateTime.parse(i.getMetadata().getCreationTimestamp(), DateTimeFormatter.ISO_DATE_TIME));
            return storageClassView;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<StorageClassView> page(String name, ListQueryParams params) {
        List<StorageClass> storageClasses = kubernetesClient.storage().storageClasses().list().getItems();
        List<StorageClassView> storageClassViews = storageClasses.stream()
                .skip((params.getCurrentPage() - 1) * params.getPageSize())
                .limit(params.getPageSize())
                .map(i -> {
                    StorageClassView storageClassView = new StorageClassView();
                    storageClassView.setName(i.getMetadata().getName());
                    storageClassView.setProvisioner(i.getProvisioner());
                    storageClassView.setReclaimPolicy(i.getReclaimPolicy());
                    storageClassView.setCreationTimestamp(LocalDateTime.parse(i.getMetadata().getCreationTimestamp(), DateTimeFormatter.ISO_DATE_TIME));
                    return storageClassView;
                }).collect(Collectors.toList());
        Page<StorageClassView> page = new Page<>();
        page.setRecords(storageClassViews);
        page.setCurrent(params.getCurrentPage());
        page.setSize(params.getPageSize());
        page.setTotal(storageClasses.size());
        return page;
    }
}
