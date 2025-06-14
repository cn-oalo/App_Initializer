package cn.oalo.framework.storage;

/**
 * 存储类型枚举
 */
public enum StorageType {
    /**
     * 本地存储
     */
    LOCAL,

    /**
     * 阿里云OSS
     */
    ALIYUN_OSS,

    /**
     * 腾讯云COS
     */
    TENCENT_COS,

    /**
     * 七牛云Kodo
     */
    QINIU_KODO,

    /**
     * MinIO
     */
    MINIO
} 