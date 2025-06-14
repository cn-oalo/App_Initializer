package cn.oalo.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化消息工具类
 */
@Component
public class MessageUtils {
    
    private static MessageSource messageSource;
    
    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }
    
    /**
     * 获取国际化消息
     *
     * @param code 消息键
     * @return 国际化消息
     */
    public static String getMessage(String code) {
        return getMessage(code, null);
    }
    
    /**
     * 获取国际化消息
     *
     * @param code 消息键
     * @param args 参数
     * @return 国际化消息
     */
    public static String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
} 