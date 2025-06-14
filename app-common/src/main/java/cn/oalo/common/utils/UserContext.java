package cn.oalo.common.utils;

/**
 * 用户上下文
 */
public class UserContext {
    
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();
    
    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }
    
    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public static String getUserId() {
        return userIdHolder.get();
    }
    
    /**
     * 清除用户ID
     */
    public static void clear() {
        userIdHolder.remove();
    }
} 