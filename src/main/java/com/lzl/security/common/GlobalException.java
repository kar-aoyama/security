package com.lzl.security.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lzl
 * @ClassName GlobalException
 * @date: 2021/4/12 下午5:27
 * @Description:
 */
@Setter
@Getter
public class GlobalException extends Exception {

    private String tips;

    private String message;

    private String code;


    private GlobalException() {
    }

    private GlobalException(String tips, String message) {
        this.tips = tips;
        this.message = message;
    }

    private GlobalException(String message) {
        this.message = message;
    }

    public static GlobalException newInstance(String tips, String message) {
        return new GlobalException(tips, message);
    }

    public static GlobalException newInstance(String message) {
        return new GlobalException(message);
    }

    public class ValidMessage {

    }
}
