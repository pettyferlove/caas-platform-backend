package com.github.pettyfer.caas.framework.system.restful;


import com.github.pettyfer.caas.global.constants.ApiConstant;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
