package com.github.pettyfer.caas.framework.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.system.entity.SystemMessage;
import com.github.pettyfer.caas.framework.system.mapper.SystemMessageMapper;
import com.github.pettyfer.caas.framework.system.model.UserMessage;
import com.github.pettyfer.caas.framework.system.service.ISystemMessageService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 系统消息 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-23
 */
@Service
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageMapper, SystemMessage> implements ISystemMessageService {

    @Override
    public List<UserMessage> loadUnread(String userId) {
        List<SystemMessage> list = this.list(Wrappers.<SystemMessage>lambdaQuery().orderByDesc(SystemMessage::getCreateTime)
                .eq(SystemMessage::getReceiver, userId)
                .eq(SystemMessage::getState, 0)
                .last("limit 10")
        );
        Optional<List<UserMessage>> optionalUserGeneralMessages = Optional.ofNullable(ConverterUtil.convertList(SystemMessage.class, UserMessage.class, list));
        return optionalUserGeneralMessages.orElseGet(ArrayList::new);
    }

    @Override
    public String create(SystemMessage systemMessage) {
        if (this.save(systemMessage)) {
            return systemMessage.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean read(String userId, String id) {
        SystemMessage systemMessage = new SystemMessage();
        systemMessage.setId(id);
        systemMessage.setState(1);
        systemMessage.setModifier(userId);
        return this.updateById(systemMessage);
    }

}
