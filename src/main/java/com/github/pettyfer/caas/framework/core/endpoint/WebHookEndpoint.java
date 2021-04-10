package com.github.pettyfer.caas.framework.core.endpoint;

import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.global.constants.BuildStatus;
import com.github.pettyfer.caas.framework.core.event.GitlabPushDetails;
import com.github.pettyfer.caas.framework.core.event.publisher.GitlabPushEventPublisher;
import com.github.pettyfer.caas.framework.core.event.publisher.ProjectBuildEventPublisher;
import com.github.pettyfer.caas.framework.core.event.publisher.SqlBuildEventPublisher;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Pettyfer
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/hooks")
public class WebHookEndpoint {

    private final GitlabPushEventPublisher gitlabPushEventPublisher;

    private final SqlBuildEventPublisher sqlBuildEventPublisher;

    private final ProjectBuildEventPublisher projectBuildEventPublisher;

    public WebHookEndpoint(GitlabPushEventPublisher gitlabPushEventPublisher, SqlBuildEventPublisher sqlBuildEventPublisher, ProjectBuildEventPublisher projectBuildEventPublisher) {
        this.gitlabPushEventPublisher = gitlabPushEventPublisher;
        this.sqlBuildEventPublisher = sqlBuildEventPublisher;
        this.projectBuildEventPublisher = projectBuildEventPublisher;
    }

    @SneakyThrows
    @RequestMapping("gitlab/{buildId}")
    public HttpEntity<String> gitlab(HttpServletRequest request, @PathVariable String buildId) {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String body = sb.toString();
        GitlabPushDetails gitlabPushDetails = JSONObject.toJavaObject(JSONObject.parseObject(body), GitlabPushDetails.class);
        gitlabPushEventPublisher.push(buildId, gitlabPushDetails);
        return new HttpEntity<>(null);
    }

    @SneakyThrows
    @RequestMapping("build/sql/{jobId}")
    public HttpEntity<String> sqlBuild(HttpServletRequest request, @PathVariable String jobId, BuildStatus status) {
        sqlBuildEventPublisher.push(jobId, status);
        return new HttpEntity<>(null);
    }

    @SneakyThrows
    @RequestMapping("build/project/{jobId}")
    public HttpEntity<String> projectBuild(HttpServletRequest request, @PathVariable String jobId, BuildStatus status) {
        projectBuildEventPublisher.push(jobId, status);
        return new HttpEntity<>(null);
    }

}
