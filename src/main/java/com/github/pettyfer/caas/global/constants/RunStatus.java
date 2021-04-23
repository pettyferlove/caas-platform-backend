package com.github.pettyfer.caas.global.constants;

public enum RunStatus {
    Preparing("preparing", "准备中"),
    Error("error", "运行错误"),
    Stopped("stopped", "已停止"),
    Stopping("stopping", "停止中"),
    Updating("updating", "更新中"),
    Running("running", "运行中");


    private final String value;
    private final String name;

    RunStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }
}
