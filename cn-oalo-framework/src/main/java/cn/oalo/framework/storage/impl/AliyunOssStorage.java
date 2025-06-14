package cn.oalo.framework.storage.impl;

import cn.oalo.framework.config.OssConfig;
import cn.oalo.framework.storage.FileStorage;
import cn.oalo.framework.storage.StorageType;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 阿里云OSS存储实现
 */
@Component
public class AliyunOssStorage implements FileStorage {

    private static final Logger log = LoggerFactory.getLogger(AliyunOssStorage.class);

    @Autowired
    private OSS ossClient;

    @Autowired
    private OssConfig.OssProperties ossProperties;

    @Override
    public String upload(MultipartFile file, String fileName) throws IOException {
        return upload(file.getInputStream(), fileName);
    }

    @Override
    public String upload(InputStream inputStream, String fileName) throws IOException {
        try {
            // 上传文件到OSS
            ossClient.putObject(ossProperties.getBucketName(), fileName, inputStream);
            log.info("文件上传成功：{}", fileName);
            return fileName;
        } catch (Exception e) {
            log.error("OSS上传文件失败", e);
            throw new IOException("OSS上传文件失败", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            ossClient.deleteObject(ossProperties.getBucketName(), filePath);
            log.info("OSS删除文件成功：{}", filePath);
            return true;
        } catch (Exception e) {
            log.error("OSS删除文件失败", e);
            return false;
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        // 使用自定义域名
        if (ossProperties.getDomain() != null && !ossProperties.getDomain().isEmpty()) {
            return ossProperties.getDomain() + "/" + filePath;
        }
        // 使用默认域名
        return "https://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/" + filePath;
    }

    @Override
    public InputStream getFileStream(String filePath) throws IOException {
        try {
            OSSObject ossObject = ossClient.getObject(ossProperties.getBucketName(), filePath);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            log.error("获取OSS文件流失败", e);
            throw new IOException("获取OSS文件流失败", e);
        }
    }

    @Override
    public StorageType getType() {
        return StorageType.ALIYUN_OSS;
    }
} 