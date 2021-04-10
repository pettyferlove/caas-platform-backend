package com.github.pettyfer.caas.global.constants;

/**
 * @author Pettyfer
 */
public class EnvConstant {

    /**
     * 开发环境
     */
    public final static Integer DEVELOPMENT_ENVIRONMENT = 1;

    /**
     * 测试环境
     */
    public final static Integer TEST_ENVIRONMENT = 2;

    /**
     * 正式环境
     */
    public final static Integer PRODUCTION_ENVIRONMENT = 9;


    public static String transform(Integer type) {
        switch (type) {
            case 2:
                return "test";
            case 9:
                return "production";
            default:
                return "development";
        }
    }

}
