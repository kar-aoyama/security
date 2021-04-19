package com.lzl.security.response;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.lzl.security.common.GlobalException;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommonResponse<T> implements Serializable {

    /**
     * 成功CODE
     */
    private static final String DEFAULT_SUCCESS_CODE = "0";
    /**
     * 成功MESSAGE
     */
    private static final String DEFAULT_SUCCESS_MSG = "SUCCESS";
    private static final long serialVersionUID = -4328418729486090960L;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 应用环境
     */
    private String env;
    /**
     * 应用版本
     */
    private String version;
    /**
     * 响应码
     */
    private String code;
    /**
     * 异常信息
     */
    private String error;
    /**
     * 用户提示
     */
    private String message;
    /**
     * 参数验证错误
     */
    private List<GlobalException.ValidMessage> validMessage;
    /**
     * 响应数据
     */
    private T data;

    private CommonResponse() {
        this.applicationName = "security";
        this.env = "dev";
        this.version = "v1";
    }

    private CommonResponse(String code, String message, String error) {
        this();
        this.code = code;
        this.message = message;
        this.error = error;
    }

    /**
     * 成功响应
     *
     * @param data
     */
    private CommonResponse(T data) {
        this(DEFAULT_SUCCESS_CODE, StrUtil.EMPTY, DEFAULT_SUCCESS_MSG);
        this.data = data;
    }

    /**
     * @param e
     */
    private CommonResponse(GlobalException e, String error) {
        this();
        this.code = e.getCode();
        this.message = e.getTips();
        this.error = error;
    }

    /**
     * 用户行为导致的错误
     *
     * @param code
     * @param message
     * @param error
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> exceptionInstance(
            String code, String message, String error) {
        return new CommonResponse<T>(code, message, error);
    }

    /**
     * 正常响应
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> successInstance(T data) {
        return new CommonResponse<>(data);
    }

    /**
     * 正常响应
     *
     * @param code
     * @param message
     * @param error
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> successInstance(String code, String message, String error) {
        return new CommonResponse<T>(code, message, error);
    }

    /**
     * 正常响应
     *
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> successInstance() {
        return (CommonResponse<T>) successInstance(StrUtil.EMPTY);
    }

    /**
     * 用户行为导致的错误
     *
     * @param e
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> exceptionInstance(GlobalException e) {
        return exceptionInstance(e, e.getMessage());
    }

    /**
     * 用户行为导致的错误
     *
     * @param e
     * @param error
     * @param <T>
     * @return
     */
    public static <T> CommonResponse<T> exceptionInstance(GlobalException e, String error) {
        return new CommonResponse<T>(e, error);
    }

    /**
     * 转换成json
     *
     * @return
     */
    public String toJson() {
        String json = JSONObject.toJSONString(this);
        if (StrUtil.isBlank(json)) {
            json = StrUtil.EMPTY;
        }
        return json;
    }

    public boolean isSuccess() {
        return StrUtil.equals(this.code, DEFAULT_SUCCESS_CODE);
    }

    public boolean unSuccess() {
        return !isSuccess();
    }

}
