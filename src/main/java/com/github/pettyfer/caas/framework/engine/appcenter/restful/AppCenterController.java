package com.github.pettyfer.caas.framework.engine.appcenter.restful;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.framework.engine.appcenter.model.ReleaseOperate;
import com.github.pettyfer.caas.framework.engine.appcenter.service.IAppCenterService;
import com.github.pettyfer.caas.global.constants.ApiConstant;
import com.github.pettyfer.caas.global.model.R;
import com.github.pettyfer.caas.global.properties.AppCenterProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Pettyfer
 */
@RestController
@RequestMapping(ApiConstant.API_V1_PREFIX + "/app-center")
public class AppCenterController {

    private final AppCenterProperties properties;

    private final IAppCenterService appCenterService;

    public AppCenterController(AppCenterProperties properties, IAppCenterService appCenterService) {
        this.properties = properties;
        this.appCenterService = appCenterService;
    }

    @GetMapping("page")
    public R<JSONObject> page(String name , @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "8") Integer size) {
        return new R<JSONObject>(appCenterService.page(name, page, size));
    }

    @GetMapping("search")
    public R<JSONObject> search(String name, String version, String appVersion) {
        return new R<JSONObject>(appCenterService.search(name, version, appVersion));
    }

    @GetMapping("load-config")
    public R<String> loadConfig() {
        return new R<String>(properties.getUrl());
    }

    @GetMapping("releases/{namespace}")
    public R<JSONArray> releases(@PathVariable String namespace) {
        return new R<JSONArray>(appCenterService.releases(namespace));
    }

    @PostMapping("release/{namespace}")
    public R<JSONObject> createRelease(@PathVariable String namespace, @RequestBody @Validated ReleaseOperate release) {
        return new R<JSONObject>(appCenterService.createRelease(namespace, release));
    }

    @PutMapping("release/{namespace}")
    public R<JSONObject> updateRelease(@PathVariable String namespace, @RequestBody @Validated ReleaseOperate release) {
        return new R<JSONObject>(appCenterService.updateRelease(namespace, release));
    }

    @DeleteMapping("release/{namespace}/{releaseName}")
    public R<Boolean> deleteRelease(@PathVariable String namespace, @PathVariable String releaseName) {
        return new R<Boolean>(appCenterService.deleteRelease(namespace, releaseName));
    }

    @GetMapping("release/{namespace}/{releaseName}")
    public R<JSONObject> getRelease(@PathVariable String namespace, @PathVariable String releaseName) {
        return new R<JSONObject>(appCenterService.getRelease(namespace, releaseName));
    }

    @GetMapping("detail/{repo}/{chartName}")
    public R<JSONObject> detail(@PathVariable String repo , @PathVariable String chartName) {
        return new R<JSONObject>(appCenterService.detail(repo, chartName));
    }

    @GetMapping("versions/{repo}/{chartName}")
    public R<JSONArray> versions(@PathVariable String repo ,@PathVariable String chartName) {
        return new R<JSONArray>(appCenterService.versions(repo, chartName));
    }

    @GetMapping("readme/{repo}/{chartName}/{version}")
    public R<String> readme(@PathVariable String repo ,@PathVariable String chartName, @PathVariable String version) {
        return new R<String>(appCenterService.readme(repo, chartName, version));
    }

    @GetMapping("yaml/{repo}/{chartName}/{version}")
    public R<String> yaml(@PathVariable String repo ,@PathVariable String chartName, @PathVariable String version) {
        return new R<String>(appCenterService.yaml(repo, chartName, version));
    }

    @GetMapping("schema/{repo}/{chartName}/{version}")
    public R<String> schema(@PathVariable String repo ,@PathVariable String chartName, @PathVariable String version) {
        return new R<String>(appCenterService.schema(repo, chartName, version));
    }


}
