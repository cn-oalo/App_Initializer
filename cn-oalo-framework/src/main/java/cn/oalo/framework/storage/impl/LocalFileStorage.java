package cn.oalo.framework.storage.impl;

import cn.oalo.common.exception.BusinessException;
import cn.oalo.common.util.FileUtils;
import cn.oalo.framework.config.FileUploadConfig;
import cn.oalo.framework.storage.FileStorage;
import cn.oalo.framework.storage.StorageType;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 本地文件存储实现
 */
@Component
public class LocalFileStorage implements FileStorage {

    private static final Logger log = LoggerFactory.getLogger(LocalFileStorage.class);

    @Autowired
    private FileUploadConfig.FileUploadProperties fileUploadProperties;

    @Value("${oalo.profile:upload}")
    private String uploadPath;

    @Override
    public String upload(MultipartFile file, String fileName) throws IOException {
        // 创建目录
        String absolutePath = uploadPath + File.separator + fileUploadProperties.getPath();
        File directory = new File(absolutePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 保存文件
        File dest = new File(directory.getAbsolutePath() + File.separator + fileName);
        file.transferTo(dest);
        log.info("文件上传成功：{}", dest.getAbsolutePath());

        return fileUploadProperties.getPath() + File.separator + fileName;
    }

    @Override
    public String upload(InputStream inputStream, String fileName) throws IOException {
        // 创建目录
        String absolutePath = uploadPath + File.separator + fileUploadProperties.getPath();
        File directory = new File(absolutePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 保存文件
        Path targetPath = Paths.get(directory.getAbsolutePath(), fileName);
        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        log.info("文件上传成功：{}", targetPath.toString());

        return fileUploadProperties.getPath() + File.separator + fileName;
    }

    @Override
    public boolean delete(String filePath) {
        String absolutePath = uploadPath + File.separator + filePath;
        return FileUtils.delete(absolutePath);
    }

    @Override
    public String getFileUrl(String filePath) {
        return "/system/file/download?filePath=" + filePath;
    }

    @Override
    public InputStream getFileStream(String filePath) throws IOException {
        String absolutePath = uploadPath + File.separator + filePath;
        File file = new File(absolutePath);
        if (!file.exists()) {
            throw new BusinessException("文件不存在");
        }
        return new FileInputStream(file);
    }

    @Override
    public StorageType getType() {
        return StorageType.LOCAL;
    }
} 