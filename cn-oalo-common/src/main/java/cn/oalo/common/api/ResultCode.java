package cn.oalo.common.api;

/**
 * API返回码
 */
public enum ResultCode implements IErrorCode {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    
    /**
     * 操作失败
     */
    FAILED(500, "操作失败"),
    
    /**
     * 参数检验失败
     */
    VALIDATE_FAILED(400, "参数检验失败"),
    
    /**
     * 暂未登录或token已经过期
     */
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    
    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),
    
    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),
    
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    
    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(1001, "用户名或密码错误"),
    
    /**
     * 用户已被禁用
     */
    USER_DISABLED(1002, "用户已被禁用"),
    
    /**
     * 用户已存在
     */
    USER_ALREADY_EXISTS(1003, "用户已存在"),
    
    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(1004, "验证码错误"),
    
    /**
     * 验证码已过期
     */
    CAPTCHA_EXPIRED(1005, "验证码已过期"),
    
    /**
     * 文件上传错误
     */
    FILE_UPLOAD_ERROR(2001, "文件上传失败"),
    
    /**
     * 文件下载错误
     */
    FILE_DOWNLOAD_ERROR(2002, "文件下载失败"),
    
    /**
     * 文件删除错误
     */
    FILE_DELETE_ERROR(2003, "文件删除失败"),
    
    /**
     * 文件不存在
     */
    FILE_NOT_FOUND(2004, "文件不存在"),
    
    /**
     * 文件大小超过限制
     */
    FILE_SIZE_LIMIT(2005, "文件大小超过限制"),
    
    /**
     * 文件类型不支持
     */
    FILE_TYPE_NOT_ALLOWED(2006, "文件类型不支持");

    private final long code;
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 