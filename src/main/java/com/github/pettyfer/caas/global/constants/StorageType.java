package com.github.pettyfer.caas.global.constants;

/**
 * @author Petty
 */

public enum StorageType {

    /**
     * 阿里云OSS储存方案
     */
    CloudAliyunOSS("Aliyun Cloud OSS", "aliyun", 1001),

    /**
     * 七牛云对象储存方案
     */
    CloudQiniu("Qiniu Cloud", "qiniu", 1002),


    /**
     * 腾讯云COS方案
     */
    CloudTencent("Tencent Cloud COS", "tencent", 1003),

    /**
     * 百度云BOS方案
     */
    CloudBaidu("Baidu Cloud BOS", "baidu", 1004),

    /**
     * 本地文件储存
     */
    FileLocal("Local File", "local", 2001);

    private String type;
    private String service;
    private int value;

    StorageType(String type, String service, int value) {
        this.type = type;
        this.service = service;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getService() {
        return service;
    }

    @Override
    public String toString() {
        return "StorageType{" +
                "type='" + type + '\'' +
                ", service='" + service + '\'' +
                ", value=" + value +
                '}';
    }

    public static StorageType parse(int value) {
        for (StorageType storageType : StorageType.values()) {
            if (storageType.getValue() == value) {
                return storageType;
            }
        }

        throw new IllegalArgumentException("Unable to parse the provided type " + value);
    }

}
