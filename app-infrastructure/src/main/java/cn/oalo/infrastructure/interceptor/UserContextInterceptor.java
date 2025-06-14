package cn.oalo.infrastructure.interceptor;

import cn.oalo.common.utils.UserContext;
import cn.oalo.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户上下文拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserContextInterceptor implements HandlerInterceptor {
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 清除ThreadLocal，防止内存泄漏
        UserContext.clear();
        
        // 从请求头中获取token
        String token = resolveToken(request);
        if (token != null) {
            try {
                // 从token中获取用户ID
                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                if (userId != null) {
                    // 将用户ID放入ThreadLocal
                    UserContext.setUserId(String.valueOf(userId));
                    log.debug("设置用户上下文，用户ID: {}", userId);
                }
            } catch (Exception e) {
                log.warn("获取用户ID失败: {}", e.getMessage());
            }
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清除ThreadLocal，防止内存泄漏
        UserContext.clear();
    }
    
    /**
     * 从请求头中解析token
     *
     * @param request 请求
     * @return token
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
} 