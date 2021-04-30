package com.github.pettyfer.caas.utils;

import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Pettyfer
 */
@UtilityClass
public class YamlUtil {

    private static final Yaml YAML = new Yaml();

    public <T> T yamlToObj(String yaml, Class<T> type) {
        return (T) YAML.loadAs(yaml, type);
    }

    public String objToYaml(Object data) {
        return YAML.dumpAsMap(data);
    }

}
