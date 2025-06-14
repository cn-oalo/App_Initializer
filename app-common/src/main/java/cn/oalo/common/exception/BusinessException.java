package cn.oalo.common.exception;

import cn.oalo.common.api.IErrorCode;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private IErrorCode errorCode;
    
    public BusinessException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(Throwable cause) {
        super(cause);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public IErrorCode getErrorCode() {
        return errorCode;
    }
} 