package cn.oalo.infrastructure.oss.impl;

import cn.oalo.infrastructure.oss.OssService;
import cn.oalo.common.exception.BusinessException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Aliyun OSS服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunOssServiceImpl implements OssService {
    
    private final OSS ossClient;
    
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    
    @Value("${aliyun.oss.domain}")
    private String domain;
    
    @Override
    public String upload(MultipartFile file, String fileName) {
        try {
            return upload(file.getInputStream(), fileName);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new BusinessException("文件上传失败");
        }
    }
    
    @Override
    public String upload(InputStream inputStream, String fileName) {
        // 生成日期路径
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 生成文件名
        String fileExtension = getFileExtension(fileName);
        String objectName = datePath + "/" + UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
        
        try {
            // 上传文件
            ossClient.putObject(bucketName, objectName, inputStream);
            // 返回URL
            return domain + "/" + objectName;
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            throw new BusinessException("文件上传失败");
        }
    }
    
    @Override
    public boolean delete(String fileName) {
        try {
            // 如果是完整URL，截取对象名
            if (fileName.startsWith(domain)) {
                fileName = fileName.substring(domain.length() + 1);
            }
            // 删除文件
            ossClient.deleteObject(bucketName, fileName);
            return true;
        } catch (Exception e) {
            log.error("文件删除失败: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public InputStream download(String fileName) {
        try {
            // 如果是完整URL，截取对象名
            if (fileName.startsWith(domain)) {
                fileName = fileName.substring(domain.length() + 1);
            }
            // 下载文件
            OSSObject ossObject = ossClient.getObject(bucketName, fileName);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage());
            throw new BusinessException("文件下载失败");
        }
    }
    
    @Override
    public String getUrl(String fileName) {
        // 如果是完整URL，直接返回
        if (fileName.startsWith("http")) {
            return fileName;
        }
        // 返回URL
        return domain + "/" + fileName;
    }
    
    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return lastIndex > 0 ? fileName.substring(lastIndex) : "";
    }
} 