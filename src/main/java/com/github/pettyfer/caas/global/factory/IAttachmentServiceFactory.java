package com.github.pettyfer.caas.global.factory;

import com.github.pettyfer.caas.framework.system.service.IAttachmentService;
import com.github.pettyfer.caas.global.constants.StorageType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

/**
 * 附件提供者工厂
 *
 * @author Petty
 */
public interface IAttachmentServiceFactory extends ApplicationContextAware, InitializingBean {

    /**
     * 选择储存类型
     *
     * @param type 枚举
     * @return 上传服务或下载服务
     */
    IAttachmentService create(StorageType type);

}
