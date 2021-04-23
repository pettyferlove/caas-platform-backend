package com.github.pettyfer.caas.utils;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP解析工具类
 *
 * @author Pettyfer
 */
public class URLResolutionUtil {

    private static final Pattern IP_PATTERN = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)");
    private static final Pattern PORT_PATTERN = Pattern.compile(":(\\d+)");

    public static String ip(String uri) {
        String result = "";
        Matcher ipMatcher = IP_PATTERN.matcher(uri);
        while (ipMatcher.find()) {
            result = ipMatcher.group(1);
        }
        return result;
    }

    public static String port(String uri) {
        String result = "";
        Matcher portMatcher = PORT_PATTERN.matcher(uri);
        while (portMatcher.find()) {
            result = portMatcher.group(1);
        }
        if (StrUtil.isEmpty(result)) {
            if (uri.contains("https")) {
                result = "443";
            } else {
                result = "80";
            }
        }
        return result;
    }

}
