package com.github.pettyfer.caas.framework.engine.docker.register.service.impl;

import com.github.pettyfer.caas.framework.biz.service.IBizGlobalConfigurationService;
import com.github.pettyfer.caas.framework.core.model.GlobalConfiguration;
import com.github.pettyfer.caas.framework.engine.docker.register.model.*;
import com.github.pettyfer.caas.framework.engine.docker.register.service.IHarborService;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SslUtils;
import com.github.pettyfer.caas.utils.URLResolutionUtil;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Petty
 */
@Slf4j
@Service
public class HarborServiceImpl implements IHarborService {

    private final RestTemplate restTemplate;

    private final IBizGlobalConfigurationService bizGlobalConfigurationService;

    public HarborServiceImpl(RestTemplate restTemplate, IBizGlobalConfigurationService bizGlobalConfigurationService) {
        this.restTemplate = restTemplate;
        this.bizGlobalConfigurationService = bizGlobalConfigurationService;
    }

    @Override
    public List<RepositoryView> searchPublicRepository(String q) {
        GlobalConfiguration globalConfiguration = bizGlobalConfigurationService.loadConfig();
        Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPath(), "未配置Harbor仓库地址");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("q", q);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        ResponseEntity<Search> responseEntity = restTemplate.exchange(processHttps(globalConfiguration.getDockerRegistryPath()) + Search.URL, HttpMethod.GET, requestEntity, Search.class);
        Search body = Optional.ofNullable(responseEntity.getBody()).orElseGet(Search::new);
        List<SearchRepository> repository = body.getRepository();
        return repository.stream().map(i -> {
            String ip = URLResolutionUtil.ip(globalConfiguration.getDockerRegistryPath());
            String port = URLResolutionUtil.port(globalConfiguration.getDockerRegistryPath());
            String pullPath = ip;
            if (!"80".equals(port) && !"443".equals(port)) {
                pullPath = pullPath + ":" + port;
            }
            String repositoryName = i.getRepositoryName();
            if (pullPath.endsWith("/")) {
                pullPath = pullPath + repositoryName;
            } else {
                pullPath = pullPath + "/" + repositoryName;
            }
            return RepositoryView.builder()
                    .projectId(i.getProjectId())
                    .projectName(i.getProjectName())
                    .projectPublic(i.getProjectPublic())
                    .pullCount(i.getPullCount())
                    .pullUrl(pullPath)
                    .repositoryName(i.getRepositoryName())
                    .tagsCount(i.getTagsCount())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<RepositoryView> queryRepository(String projectId, String q) {
        GlobalConfiguration globalConfiguration = bizGlobalConfigurationService.loadConfig();
        Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPath(), "未配置Harbor仓库地址");
        HttpHeaders headers = createAuthHeaders(globalConfiguration.getDockerRegistryUsername(), globalConfiguration.getDockerRegistryPassword());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HashMap<String, String> params = new HashMap<String, String>(2);
        params.put("project_id", projectId);
        params.put("q", q);
        params.put("page_size", "5000");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<Repository>> parameterizedTypeReference = new ParameterizedTypeReference<List<Repository>>() {
        };
        ResponseEntity<List<Repository>> responseEntity = restTemplate.exchange(processHttps(globalConfiguration.getDockerRegistryPath()) + Repository.URL_GET + "?project_id={project_id}&q={q}&page_size={page_size}", HttpMethod.GET, requestEntity, parameterizedTypeReference, params);
        List<Repository> repositories = Optional.ofNullable(responseEntity.getBody()).orElseGet(ArrayList::new);
        return repositories.stream().map(i -> {
            String ip = URLResolutionUtil.ip(globalConfiguration.getDockerRegistryPath());
            String port = URLResolutionUtil.port(globalConfiguration.getDockerRegistryPath());
            String pullPath = ip;
            if (!"80".equals(port) && !"443".equals(port)) {
                pullPath = pullPath + ":" + port;
            }
            String repositoryName = i.getName();
            if (pullPath.endsWith("/")) {
                pullPath = pullPath + repositoryName;
            } else {
                pullPath = pullPath + "/" + repositoryName;
            }
            return RepositoryView.builder()
                    .projectId(i.getProjectId())
                    .pullCount(i.getPullCount())
                    .pullUrl(pullPath)
                    .repositoryName(i.getName())
                    .tagsCount(i.getTagsCount())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public List<RepositoryTagView> queryRepositoryTag(String repoName) {
        GlobalConfiguration globalConfiguration = bizGlobalConfigurationService.loadConfig();
        Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPath(), "未配置Harbor仓库地址");
        HttpHeaders headers = createAuthHeaders(globalConfiguration.getDockerRegistryUsername(), globalConfiguration.getDockerRegistryPassword());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("detail", "false");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        ParameterizedTypeReference<List<DetailedTag>> parameterizedTypeReference = new ParameterizedTypeReference<List<DetailedTag>>() {
        };
        URI uri = new URI(processHttps(globalConfiguration.getDockerRegistryPath()) + MessageFormat.format(DetailedTag.URL, cn.hutool.core.codec.Base64.decodeStr(repoName)));
        ResponseEntity<List<DetailedTag>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, parameterizedTypeReference);
        List<DetailedTag> body = Optional.ofNullable(responseEntity.getBody()).orElseGet(ArrayList::new);
        Optional<List<RepositoryTagView>> repositoryTagViews = Optional.ofNullable(ConverterUtil.convertList(DetailedTag.class, RepositoryTagView.class, body));
        List<RepositoryTagView> views = repositoryTagViews.orElseGet(ArrayList::new);
        views.sort(Comparator.comparing(RepositoryTagView::getName).reversed());
        return views;
    }

    @Override
    public Boolean checkProject(String projectName) {
        try {
            GlobalConfiguration globalConfiguration = bizGlobalConfigurationService.loadConfig();
            Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPath(), "未配置Harbor仓库地址");
            HttpHeaders headers = createAuthHeaders(globalConfiguration.getDockerRegistryUsername(), globalConfiguration.getDockerRegistryPassword());
            HashMap<String, String> params = new HashMap<String, String>(1);
            params.put("project_name", projectName);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(processHttps(globalConfiguration.getDockerRegistryPath()) + Project.URL + "?project_name={project_name}", HttpMethod.HEAD, requestEntity, String.class, params);
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException.NotFound) {
                return false;
            } else {
                throw e;
            }
        }
    }


    @Override
    public String createProject(ProjectCreate project) {
        if (!checkProject(project.getProjectName())) {
            GlobalConfiguration globalConfiguration = bizGlobalConfigurationService.loadConfig();
            Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPath(), "未配置Harbor仓库地址");
            HttpHeaders headers = createAuthHeaders(globalConfiguration.getDockerRegistryUsername(), globalConfiguration.getDockerRegistryPassword());
            headers.add("Accept", "application/json");
            headers.add("Content-Type", "application/json");
            HttpEntity<ProjectCreate> requestEntity = new HttpEntity<>(project, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(processHttps(globalConfiguration.getDockerRegistryPath()) + Project.URL, requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                return findProject(project.getProjectName());
            } else {
                throw new RuntimeException("创建仓库Project失败");
            }
        } else {
            return findProject(project.getProjectName());
        }
    }

    private String findProject(String projectName) {
        GlobalConfiguration globalConfiguration = bizGlobalConfigurationService.loadConfig();
        Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPath(), "未配置Harbor仓库地址");
        HttpHeaders headers = createAuthHeaders(globalConfiguration.getDockerRegistryUsername(), globalConfiguration.getDockerRegistryPassword());
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HashMap<String, String> params = new HashMap<String, String>(1);
        params.put("project_name", projectName);
        HttpEntity<MultiValueMap<String, String>> queryEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<Project>> parameterizedTypeReference = new ParameterizedTypeReference<List<Project>>() {
        };
        ResponseEntity<List<Project>> listResponseEntity = restTemplate.exchange(processHttps(globalConfiguration.getDockerRegistryPath()) + Project.URL + "?name={project_name}", HttpMethod.GET, queryEntity, parameterizedTypeReference, params);
        List<Project> body = listResponseEntity.getBody();
        assert body != null;
        Project findProject = body.stream().filter(p -> p.getName().equals(projectName)).findAny().orElse(null);
        assert findProject != null;
        return findProject.getProjectId().toString();
    }

    @Override
    public String createProject(String projectName) {
        ProjectCreate pc = new ProjectCreate();
        pc.setProjectName(projectName);
        pc.setCountLimit(-1L);
        pc.setStorageLimit(-1L);
        return createProject(pc);
    }

    @Override
    public Boolean removeProject(String projectId) {
        return null;
    }

    public static HttpHeaders createAuthHeaders(String username, String password) {
        return new HttpHeaders() {
            private static final long serialVersionUID = 2393630277970205061L;

            {
                String auth = username + ":" + password;
                String authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
                set("Authorization", authHeader);
            }
        };
    }

    private URL processHttps(String path) {
        URL url = null;
        try {
            url = new URL(path);
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                SslUtils.ignoreSsl();
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return url;
    }

    ;

}
