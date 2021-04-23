package com.github.pettyfer.caas.framework.system.restful;


import com.github.pettyfer.caas.framework.system.model.UserMessage;
import com.github.pettyfer.caas.framework.system.service.ISystemMessageService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 系统消息 接口控制器
 * </p>
 *
 * @author Petty
 * @since 2019-06-27
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/message")
public class MessageController {

    private final ISystemMessageService systemMessageService;

    public MessageController(ISystemMessageService systemMessageService) {
        this.systemMessageService = systemMessageService;
    }

    @GetMapping("load/unread")
    public R<List<UserMessage>> loadUnread() {
        return new R<>(systemMessageService.loadUnread(Objects.requireNonNull(SecurityUtil.getUser()).getId()));
    }

    @PutMapping("/read/{id}")
    public R<Boolean> get(@PathVariable String id) {
        return new R<>(systemMessageService.read(Objects.requireNonNull(SecurityUtil.getUser()).getId(), id));
    }
}
