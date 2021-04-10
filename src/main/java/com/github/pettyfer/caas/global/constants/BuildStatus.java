package com.github.pettyfer.caas.global.constants;

/**
 * @author Pettyfer
 */

public enum BuildStatus {
    Preparing("preparing", "准备中"),
    Building("building", "构建中"),
    Success("success", "构建成功"),
    Fail("fail", "构建失败"),
    Send("send", "已发送任务"),
    Stopped("stopped", "已停止"),
    Finish("finish", "已结束"),
    Running("running", "运行中");


    private final String value;
    private final String name;

    BuildStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }
}
