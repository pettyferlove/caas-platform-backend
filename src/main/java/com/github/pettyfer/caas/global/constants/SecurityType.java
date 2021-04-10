package com.github.pettyfer.caas.global.constants;

/**
 * @author Petty
 */

public enum SecurityType {

    /**
     * 文件私有读写
     */
    Private("private", 5001),


    /**
     * 私有写公有读
     */
    PublicRead("public-read", 5002);

    private String acl;
    private int value;

    SecurityType(String acl, int value) {
        this.acl = acl;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SecurityType{" +
                "acl='" + acl + '\'' +
                '}';
    }

}
