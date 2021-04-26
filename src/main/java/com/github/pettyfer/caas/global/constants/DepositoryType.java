package com.github.pettyfer.caas.global.constants;

/**
 * @author Pettyfer
 */

public enum DepositoryType {

    Subversion("subversion", "SVN"),
    GitLabV4("gitlab_v4", "GitLab V4");


    private final String value;
    private final String name;

    DepositoryType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

}
