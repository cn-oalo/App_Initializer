package cn.oalo.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 文件上传配置
 */
@Configuration
public class FileUploadConfig {

    /**
     * 文件上传解析器
     */
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        // 设置上传文件的最大大小，单位为字节（50MB）
        resolver.setMaxUploadSize(50 * 1024 * 1024);
        // 设置单个文件的最大大小，单位为字节（10MB）
        resolver.setMaxUploadSizePerFile(10 * 1024 * 1024);
        // 设置默认编码
        resolver.setDefaultEncoding("UTF-8");
        // 设置内存阈值，大于这个值的文件会被写入磁盘，单位为字节（1MB）
        resolver.setMaxInMemorySize(1024 * 1024);
        return resolver;
    }
    
    /**
     * 文件上传属性配置
     */
    @Bean
    @ConfigurationProperties(prefix = "file.upload")
    public FileUploadProperties fileUploadProperties() {
        return new FileUploadProperties();
    }
    
    /**
     * 文件上传属性类
     */
    public static class FileUploadProperties {
        /**
         * 上传路径
         */
        private String path = "upload";
        
        /**
         * 允许的文件类型
         */
        private String[] allowedTypes = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt", ".zip", ".rar"};
        
        /**
         * 允许的最大文件大小（单位：MB）
         */
        private int maxSize = 10;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String[] getAllowedTypes() {
            return allowedTypes;
        }

        public void setAllowedTypes(String[] allowedTypes) {
            this.allowedTypes = allowedTypes;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
    }
} 