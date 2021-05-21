package com.github.pettyfer.caas.framework.engine.appcenter.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.framework.engine.appcenter.model.ReleaseOperate;
import com.github.pettyfer.caas.framework.engine.appcenter.service.IAppCenterService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.global.properties.AppCenterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class AppCenterServiceImpl implements IAppCenterService {

    private final AppCenterProperties properties;

    private final RestTemplate restTemplate;

    public AppCenterServiceImpl(AppCenterProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    @Override
    public JSONObject page(String name, Integer page, Integer size) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("page", page);
        params.put("size", size);
        params.put("q", name);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/assetsvc/v1/clusters/default/namespaces/kubeapps/charts?q={q}&page={page}&size={size}", HttpMethod.GET, requestEntity, parameterizedTypeReference, params);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            JSONObject result = new JSONObject();
            result.put("record", jsonObject.getJSONArray("data"));
            result.put("meta", jsonObject.getJSONObject("meta"));
            return result;
        } else {
            throw new BaseRuntimeException("获取应用列表失败");
        }
    }

    @Override
    public JSONArray versions(String repo, String chartName) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/assetsvc/v1/clusters/default/namespaces/kubeapps/charts/" + repo + "/" + chartName + "/versions", HttpMethod.GET, requestEntity, parameterizedTypeReference);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            return jsonObject.getJSONArray("data");
        } else {
            throw new BaseRuntimeException("获取应用版本列表失败");
        }
    }

    @Override
    public String readme(String repo, String chartName, String version) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.add("Accept", "text/plain");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/assetsvc/v1/clusters/default/namespaces/kubeapps/assets/" + repo + "/" + chartName + "/versions/" + version + "/README.md", HttpMethod.GET, requestEntity, parameterizedTypeReference);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new BaseRuntimeException("获取应用说明文档失败");
        }
    }

    @Override
    public JSONObject detail(String repo, String chartName) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/assetsvc/v1/clusters/default/namespaces/kubeapps/charts/" + repo + "/" + chartName, HttpMethod.GET, requestEntity, parameterizedTypeReference);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            return jsonObject.getJSONObject("data");
        } else {
            throw new BaseRuntimeException("获取应用详情失败");
        }
    }

    @Override
    public String yaml(String repo, String chartName, String version) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.add("Accept", "text/plain");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/assetsvc/v1/clusters/default/namespaces/kubeapps/assets/" + repo + "/" + chartName + "/versions/" + version + "/values.yaml", HttpMethod.GET, requestEntity, parameterizedTypeReference);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new BaseRuntimeException("获取应用Yaml文档失败");
        }
    }

    @Override
    public String schema(String repo, String chartName, String version) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.add("Accept", "text/plain");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/assetsvc/v1/clusters/default/namespaces/kubeapps/assets/" + repo + "/" + chartName + "/versions/" + version + "/values.schema.json", HttpMethod.GET, requestEntity, parameterizedTypeReference);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new BaseRuntimeException("获取应用Schema文档失败");
        }
    }

    @Override
    public JSONArray releases(String namespace) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/kubeops/v1/clusters/default/namespaces/" + namespace + "/releases", HttpMethod.GET, requestEntity, parameterizedTypeReference);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            return jsonObject.getJSONArray("data");
        } else {
            throw new BaseRuntimeException("获取应用列表失败");
        }
    }

    @Override
    public JSONObject getRelease(String namespace, String releaseName) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/kubeops/v1/clusters/default/namespaces/" + namespace + "/releases/" + releaseName, HttpMethod.GET, requestEntity, parameterizedTypeReference);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            return jsonObject.getJSONObject("data");
        } else {
            throw new BaseRuntimeException("获取应用详情失败");
        }
    }

    @Override
    public JSONObject createRelease(String namespace, ReleaseOperate release) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HttpEntity<ReleaseOperate> requestEntity = new HttpEntity<>(release, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(properties.getUrl() + "/api/kubeops/v1/clusters/default/namespaces/" + namespace + "/releases", requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            return jsonObject.getJSONObject("data");
        } else {
            throw new BaseRuntimeException("创建应用失败");
        }
    }

    @Override
    public JSONObject updateRelease(String namespace, ReleaseOperate release) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HttpEntity<ReleaseOperate> requestEntity = new HttpEntity<>(release, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/kubeops/v1/clusters/default/namespaces/" + namespace + "/releases/" + release.getReleaseName() + "?action=upgrade", HttpMethod.PUT, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            return jsonObject.getJSONObject("data");
        } else {
            throw new BaseRuntimeException("更新应用失败");
        }
    }

    @Override
    public Boolean deleteRelease(String namespace, String releaseName) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HashMap<String, String> params = new HashMap<String, String>(2);
        params.put("purge", "true");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/kubeops/v1/clusters/default/namespaces/" + namespace + "/releases/" + releaseName + "?purge={purge}", HttpMethod.DELETE, requestEntity, String.class, params);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            throw new BaseRuntimeException("获取应用详情失败");
        }
    }

    @Override
    public JSONObject search(String name, String version, String appVersion) {
        HttpHeaders headers = createAuthHeaders(properties.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("name", name);
        params.put("version", version);
        params.put("appVersion", appVersion);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<String> parameterizedTypeReference = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + "/api/assetsvc/v1/clusters/default/namespaces/kubeapps/charts?name={name}&version={version}&appVersion={appVersion}", HttpMethod.GET, requestEntity, parameterizedTypeReference, params);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            JSONObject result = new JSONObject();
            result.put("record", jsonObject.getJSONArray("data"));
            result.put("meta", jsonObject.getJSONObject("meta"));
            return result;
        } else {
            throw new BaseRuntimeException("搜索应用列表失败");
        }
    }

    public static HttpHeaders createAuthHeaders(String token) {
        return new HttpHeaders() {
            private static final long serialVersionUID = 2393630277970205061L;

            {
                String authHeader = "Bearer " + token;
                set("Authorization", authHeader);
            }
        };
    }


}
