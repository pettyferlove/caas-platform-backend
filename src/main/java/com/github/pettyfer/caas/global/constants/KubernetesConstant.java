package com.github.pettyfer.caas.global.constants;

/**
 * @author Pettyfer
 */
public interface KubernetesConstant {

    String ROOT_LABEL = "caas";

    String DESCRIPTION_LABEL = ROOT_LABEL + "-description";

    String VERSION_LABEL = ROOT_LABEL + "-version";

    String ENVIRONMENT_LABEL = ROOT_LABEL + "-environment";

    /**
     * 全局标签
     */
    String GLOBAL_LABEL = ROOT_LABEL + "-app";

    String K8S_LABEL = "k8s-app";

}
