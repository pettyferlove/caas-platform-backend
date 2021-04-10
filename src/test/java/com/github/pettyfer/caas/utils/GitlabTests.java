package com.github.pettyfer.caas.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabProjectHook;
import org.gitlab.api.models.GitlabSSHKey;
import org.gitlab.api.models.GitlabUser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class GitlabTests {

    private final String HOOKS_ENDPOINT = "http://192.168.51.67:8885/api/v1/hooks/gitlab/";

    @Test
    @SneakyThrows
    public void gitlabProjectsTest() {
        GitlabAPI connect = GitlabAPI.connect("http://gitlab.ggjs.sinobest.cn/", "9bNYZjgbzGEt7dZv8y5K");
        /*GitlabProject project = connect.getProject(965);
        System.out.println(project.getHttpUrl());
        String projectHookUrl = HOOKS_ENDPOINT + project.getName();
        List<GitlabProjectHook> projectHooks = connect.getProjectHooks(project);
        List<GitlabProjectHook> collect = projectHooks.stream().filter(i -> projectHookUrl.equals(i.getUrl())).collect(Collectors.toList());
        if(!(collect.size() >0)){
            GitlabProjectHook gitlabProjectHook = connect.addProjectHook(project, projectHookUrl);
            System.out.println(gitlabProjectHook.getId());
        }
        GitlabProjectHook hook = new GitlabProjectHook();
        hook.setId("55");
        hook.setProjectId(965);
        connect.deleteProjectHook(hook);*/
        GitlabUser user = connect.getUser();
        connect.deleteSSHKey(user.getId(), 193);
        /*GitlabSSHKey sshKey = connect.createSSHKey("my-caas", "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQDJ2PueyU8WrwT8kQLRyZndKVbnZxgG1K/dHe+eudmaE0t6rK36Vg8ghLpbx1wt2j5m78/VF8BUk6stKznXI8u1Es/ITsZvLNwBF9JcooOVSb9Jo6iLcRFGB8B6iE/jMvWXEo2bhH0M1KeeEpxqFnV4YHLuv5prES+dJ1c/Xt9Xsw==");
        System.out.println(JSONObject.toJSONString(sshKey));*/
    }

}
