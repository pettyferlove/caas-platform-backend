package com.github.pettyfer.caas.framework.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Pettyfer
 */
public class BuildEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private String autoBuildId;

    /**
     * 自动构建项目名
     */
    private String imageName;

    /**
     * 镜像仓库别名
     */
    private String tag;

    public BuildEvent(Object source,String autoBuildId, String imageName, String tag) {
        super(source);
        this.autoBuildId = autoBuildId;
        this.imageName = imageName;
        this.tag = tag;
    }

    public String getAutoBuildId() {
        return autoBuildId;
    }

    public void setAutoBuildId(String autoBuildId) {
        this.autoBuildId = autoBuildId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
