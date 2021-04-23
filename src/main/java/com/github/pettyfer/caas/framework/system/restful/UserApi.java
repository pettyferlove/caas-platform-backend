package com.github.pettyfer.caas.framework.system.restful;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.system.entity.UserListView;
import com.github.pettyfer.caas.framework.system.model.UserDetailsView;
import com.github.pettyfer.caas.framework.system.model.UserInitConfig;
import com.github.pettyfer.caas.framework.system.service.ISystemUserService;
import com.github.pettyfer.caas.framework.system.service.IUserListViewService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 用户信息 接口控制器
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
@Api(value = "用户信息", tags = {"用户信息接口"})
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/user")
public class UserApi {

    private final IUserListViewService userListViewService;

    private final ISystemUserService systemUserService;

    public UserApi(IUserListViewService userListViewService, ISystemUserService systemUserService) {
        this.userListViewService = userListViewService;
        this.systemUserService = systemUserService;
    }

    @GetMapping("page")
    public R<IPage<UserListView>> page(UserListView user, Page<UserListView> page) {
        return new R<IPage<UserListView>>(userListViewService.page(user, page));
    }

    @GetMapping("/{id}")
    public R<UserDetailsView> get(@PathVariable String id) {
        return new R<UserDetailsView>(systemUserService.get(id));
    }

    @PutMapping
    public R<Boolean> update(@Valid @RequestBody UserDetailsView userDetails) {
        return new R<Boolean>(systemUserService.update(userDetails));
    }

    @PostMapping
    public R<String> create(@Valid @RequestBody UserDetailsView userDetails) {
        return new R<String>(systemUserService.create(userDetails));
    }

    @GetMapping("/check/config")
    public R<Boolean> checkConfig() {
        return new R<Boolean>(systemUserService.checkConfig());
    }

    @PostMapping("/init/config")
    public R<Boolean> initConfig(@RequestBody UserInitConfig initConfig) {
        return new R<>(systemUserService.initConfig(initConfig));
    }

}
