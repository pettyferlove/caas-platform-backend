package com.github.pettyfer.caas.global.constants;

/**
 * @author Petty
 */
public interface SecurityConstant {

    /**
     * 角色前缀
     */
    String ROLE_PREFIX = "ROLE_";

    /**
     * 基础用户角色
     */
    String BASE_ROLE = "USER";


    /**
     * 用户无效
     */
    int STATUS_INVALID = 0;
    /**
     * 用户正常
     */
    int STATUS_NORMAL = 1;
    /**
     * 用户锁定
     */
    int STATUS_LOCK = 9;

    /**
     * 用户凭证无效
     */
    int STATUS_CREDENTIALS_EXPIRED = 8;

}
