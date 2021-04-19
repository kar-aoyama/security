package com.lzl.security.enums;

/**
 * @author lzl
 * @ClassName UrlType
 * @date: 2021/4/14 下午4:35
 * @Description:
 */
public enum UrlType {


    NO_AUTHORITY("无需权限"),
    AUTHORITY("需要权限"),
    ;


    UrlType(String description) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
