package cn.oalo.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 安全工具类
 */
@Slf4j
public class SecurityUtils {
    
    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        // 从ThreadLocal中获取用户ID，这里需要配合拦截器使用
        String userId = UserContext.getUserId();
        if (userId != null) {
            return userId;
        }
        
        // 如果ThreadLocal中没有，则从Authentication中获取
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return principal.toString();
        }
        
        return null;
    }
    
    /**
     * 获取当前用户名
     *
     * @return 当前用户名
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        return authentication.getName();
    }
} 