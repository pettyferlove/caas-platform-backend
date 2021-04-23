package com.github.pettyfer.caas.framework.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.system.entity.SystemMessage;
import com.github.pettyfer.caas.framework.system.model.UserMessage;

import java.util.List;

/**
 * <p>
 * 系统消息 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-23
 */
public interface ISystemMessageService extends IService<SystemMessage> {

    /**
     * List查找
     * @param userId userId
     * @return List 返回结果
     */
    List<UserMessage> loadUnread(String userId);

    /**
     * 创建数据
     *
     * @param systemMessage 要创建的对象
     * @return Boolean
     */
    String create(SystemMessage systemMessage);

    /**
     * 变更状态为已读
     *
     * @param userId userId
     * @param id 消息ID
     * @return Boolean
     */
    Boolean read(String userId, String id);

}
