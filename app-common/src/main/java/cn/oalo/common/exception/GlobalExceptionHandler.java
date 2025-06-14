package cn.oalo.common.exception;

import cn.oalo.common.api.R;
import cn.oalo.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return R.failed(e.getMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R<Void> handleValidException(MethodArgumentNotValidException e) {
        handleBindingResult(e.getBindingResult());
        return R.validateFailed(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(value = BindException.class)
    public R<Void> handleBindException(BindException e) {
        handleBindingResult(e.getBindingResult());
        return R.validateFailed(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R<Void> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("参数验证失败: {}", e.getMessage());
        return R.validateFailed(e.getMessage());
    }

    /**
     * 处理未知的运行时异常
     */
    @ExceptionHandler(value = RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return R.failed("系统运行异常，请联系管理员");
    }

    /**
     * 处理所有异常
     */
    @ExceptionHandler(value = Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return R.failed("系统异常，请联系管理员");
    }

    private void handleBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldError();
            log.warn("参数校验异常: {}, 字段: {}, 错误信息: {}",
                    fieldError.getObjectName(),
                    fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
    }
} 