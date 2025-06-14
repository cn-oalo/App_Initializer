package cn.oalo.common.api;

import java.io.Serializable;

/**
 * 通用响应对象
 *
 * @param <T> 数据类型
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 数据对象
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public static <T> R<T> ok() {
        return restResult(null, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), true);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), true);
    }

    public static <T> R<T> ok(T data, String message) {
        return restResult(data, ResultCode.SUCCESS.getCode(), message, true);
    }

    public static <T> R<T> failed() {
        return restResult(null, ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), false);
    }

    public static <T> R<T> failed(String message) {
        return restResult(null, ResultCode.FAILED.getCode(), message, false);
    }

    public static <T> R<T> failed(IErrorCode errorCode) {
        return restResult(null, errorCode.getCode(), errorCode.getMessage(), false);
    }

    public static <T> R<T> failed(IErrorCode errorCode, String message) {
        return restResult(null, errorCode.getCode(), message, false);
    }

    public static <T> R<T> failed(Integer code, String message) {
        return restResult(null, code, message, false);
    }

    private static <T> R<T> restResult(T data, Integer code, String message, boolean success) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMessage(message);
        r.setSuccess(success);
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public R<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public R<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public R<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public R<T> setData(T data) {
        this.data = data;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public R<T> setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
} 