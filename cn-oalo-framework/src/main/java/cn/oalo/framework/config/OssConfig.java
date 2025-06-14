package cn.oalo.framework.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS配置类
 */
@Configuration
public class OssConfig {

    /**
     * 创建OSS客户端
     */
    @Bean
    public OSS ossClient(OssProperties ossProperties) {
        return new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret());
    }

    /**
     * OSS属性配置
     */
    @Bean
    @ConfigurationProperties(prefix = "oss")
    public OssProperties ossProperties() {
        return new OssProperties();
    }

    /**
     * OSS属性类
     */
    public static class OssProperties {
        /**
         * 访问域名
         */
        private String endpoint;

        /**
         * 访问身份ID
         */
        private String accessKeyId;

        /**
         * 访问身份密钥
         */
        private String accessKeySecret;

        /**
         * 存储空间名称
         */
        private String bucketName;

        /**
         * 访问域名
         */
        private String domain;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }
} 