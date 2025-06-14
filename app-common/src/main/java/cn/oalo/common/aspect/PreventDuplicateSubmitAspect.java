package cn.oalo.common.aspect;

import cn.oalo.common.annotation.PreventDuplicateSubmit;
import cn.oalo.common.exception.BusinessException;
import cn.oalo.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 防止重复提交切面
 */
@Slf4j
@Aspect
@Component
public class PreventDuplicateSubmitAspect {
    
    /**
     * 用于存储提交记录的缓存
     * key: 用户ID + 请求方法 + 请求URL + 请求参数的hash
     * value: 计数器
     */
    private static final Map<String, AtomicInteger> CACHE = new ConcurrentHashMap<>();
    
    /**
     * 定义切点
     */
    @Pointcut("@annotation(preventDuplicateSubmit)")
    public void pointCut(PreventDuplicateSubmit preventDuplicateSubmit) {
    }
    
    /**
     * 环绕通知
     */
    @Around(value = "pointCut(preventDuplicateSubmit)", argNames = "joinPoint,preventDuplicateSubmit")
    public Object around(ProceedingJoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) throws Throwable {
        // 获取请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        
        // 获取用户ID，如果未登录则使用IP
        String userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            userId = request.getRemoteAddr();
        }
        
        // 获取请求方法
        String method = request.getMethod();
        // 获取请求URL
        String url = request.getRequestURI();
        // 获取请求参数的hash
        int paramsHash = Objects.hash(joinPoint.getArgs());
        
        // 组装缓存key
        String key = userId + ":" + method + ":" + url + ":" + paramsHash;
        
        // 判断是否重复提交
        if (CACHE.containsKey(key)) {
            // 如果已存在，则判断是否为重复提交
            AtomicInteger count = CACHE.get(key);
            if (count.get() > 0) {
                log.warn("检测到重复提交：用户[{}], 方法[{}], 路径[{}]", userId, method, url);
                throw new BusinessException(preventDuplicateSubmit.message());
            }
        } else {
            // 如果不存在，则添加到缓存
            CACHE.put(key, new AtomicInteger(1));
        }
        
        try {
            // 执行目标方法
            return joinPoint.proceed();
        } finally {
            // 计数器设置为0
            CACHE.get(key).set(0);
            
            // 设置过期时间，过期后删除缓存
            new Thread(() -> {
                try {
                    Thread.sleep(preventDuplicateSubmit.expireMillis());
                    CACHE.remove(key);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
} 