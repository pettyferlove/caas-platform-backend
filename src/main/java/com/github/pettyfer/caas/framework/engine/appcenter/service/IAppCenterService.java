package com.github.pettyfer.caas.framework.engine.appcenter.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.framework.engine.appcenter.model.ReleaseOperate;

/**
 * @author Pettyfer
 */
public interface IAppCenterService {

    JSONObject page(String name, Integer page, Integer size);

    JSONArray versions(String repo, String chartName);

    String readme(String repo, String chartName, String version);

    JSONObject detail(String repo, String chartName);

    String yaml(String repo, String chartName, String version);

    String schema(String repo, String chartName, String version);

    JSONArray releases(String namespace);

    JSONObject getRelease(String namespace, String releaseName);

    JSONObject createRelease(String namespace, ReleaseOperate release);

    JSONObject updateRelease(String namespace, ReleaseOperate release);

    Boolean deleteRelease(String namespace, String releaseName);

    JSONObject search(String name, String version, String appVersion);
}
