package cn.oalo.system.service.impl;

import cn.oalo.common.exception.BusinessException;
import cn.oalo.common.util.FileUtils;
import cn.oalo.common.util.MimeTypeUtils;
import cn.oalo.framework.config.FileUploadConfig;
import cn.oalo.framework.redis.RedisCache;
import cn.oalo.framework.storage.FileStorage;
import cn.oalo.framework.storage.FileStorageFactory;
import cn.oalo.framework.storage.StorageType;
import cn.oalo.system.entity.SysFile;
import cn.oalo.system.mapper.SysFileMapper;
import cn.oalo.system.service.SysFileService;
import cn.oalo.system.vo.SysFileVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文件服务实现类
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    private static final Logger log = LoggerFactory.getLogger(SysFileServiceImpl.class);

    @Autowired
    private SysFileMapper fileMapper;

    @Autowired
    private FileUploadConfig.FileUploadProperties fileUploadProperties;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private FileStorageFactory fileStorageFactory;
    
    @Value("${file.storage.type:LOCAL}")
    private String storageType;

    /**
     * 文件缓存键前缀
     */
    private static final String CACHE_FILE_KEY = "sys:file:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFileVO upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        // 计算文件MD5
        String fileMd5 = calculateMd5(file);

        // 检查是否已存在相同的文件（秒传功能）
        SysFile existFile = fileMapper.selectFileByMd5(fileMd5);
        if (existFile != null) {
            log.info("文件已存在，直接返回：{}", existFile.getFileName());
            return convertToVO(existFile);
        }

        // 获取存储类型
        StorageType type;
        try {
            type = StorageType.valueOf(storageType.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = StorageType.LOCAL;
        }
        
        // 获取存储策略
        FileStorage storage = fileStorageFactory.getFileStorage(type);

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = System.currentTimeMillis() + "." + extension;
        
        // 上传文件
        String filePath = storage.upload(file, fileName);

        // 保存文件信息
        SysFile sysFile = new SysFile();
        sysFile.setFileName(fileName);
        sysFile.setOriginalName(originalFilename);
        sysFile.setFilePath(filePath);
        sysFile.setFileType(extension);
        sysFile.setFileSize(file.getSize());
        sysFile.setFileMd5(fileMd5);
        sysFile.setStorageType(type.ordinal());
        sysFile.setStatus(0);
        sysFile.setDelFlag(0);
        sysFile.setCreateTime(LocalDateTime.now());

        // 保存到数据库
        fileMapper.insert(sysFile);

        // 返回文件信息
        return convertToVO(sysFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysFileVO> batchUpload(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        List<SysFileVO> result = new ArrayList<>();
        for (MultipartFile file : files) {
            result.add(upload(file));
        }
        return result;
    }

    @Override
    public void download(Long fileId, HttpServletResponse response) throws IOException {
        // 获取文件信息
        SysFile sysFile = getFileById(fileId);
        if (sysFile == null) {
            throw new BusinessException("文件不存在");
        }

        // 获取存储策略
        StorageType type = StorageType.values()[sysFile.getStorageType()];
        FileStorage storage = fileStorageFactory.getFileStorage(type);
        
        // 获取文件流
        InputStream inputStream = storage.getFileStream(sysFile.getFilePath());
        
        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(sysFile.getOriginalName(), "UTF-8"));
        
        // 写入响应
        try (InputStream is = inputStream;
             java.io.OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            log.error("下载文件失败", e);
            throw new BusinessException("下载文件失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFile(Long fileId) {
        // 获取文件信息
        SysFile sysFile = getFileById(fileId);
        if (sysFile == null) {
            throw new BusinessException("文件不存在");
        }

        // 获取存储策略
        StorageType type = StorageType.values()[sysFile.getStorageType()];
        FileStorage storage = fileStorageFactory.getFileStorage(type);
        
        // 删除物理文件
        boolean result = storage.delete(sysFile.getFilePath());

        // 删除数据库记录（逻辑删除）
        if (result) {
            sysFile.setDelFlag(1);
            sysFile.setUpdateTime(LocalDateTime.now());
            fileMapper.updateById(sysFile);

            // 清除缓存
            clearFileCache(fileId);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteFile(List<Long> fileIds) {
        if (fileIds == null || fileIds.isEmpty()) {
            return false;
        }

        boolean result = true;
        for (Long fileId : fileIds) {
            if (!deleteFile(fileId)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public SysFileVO getFileInfo(Long fileId) {
        SysFile sysFile = getFileById(fileId);
        if (sysFile == null) {
            throw new BusinessException("文件不存在");
        }
        return convertToVO(sysFile);
    }

    @Override
    public List<SysFileVO> listFilesByName(String fileName) {
        List<SysFile> files;
        if (StringUtils.isBlank(fileName)) {
            // 查询所有文件
            LambdaQueryWrapper<SysFile> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysFile::getDelFlag, 0)
                    .orderByDesc(SysFile::getCreateTime);
            files = fileMapper.selectList(queryWrapper);
        } else {
            // 根据文件名查询
            files = fileMapper.selectFilesByName(fileName);
        }
        return files.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    /**
     * 根据文件ID获取文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    private SysFile getFileById(Long fileId) {
        // 先从缓存中获取
        String cacheKey = CACHE_FILE_KEY + "id:" + fileId;
        SysFile sysFile = redisCache.getCacheObject(cacheKey);
        if (sysFile != null) {
            return sysFile;
        }

        // 从数据库中获取
        sysFile = fileMapper.selectFileById(fileId);
        if (sysFile != null) {
            // 放入缓存
            redisCache.setCacheObject(cacheKey, sysFile, 1, TimeUnit.HOURS);
        }
        return sysFile;
    }

    /**
     * 清除文件缓存
     *
     * @param fileId 文件ID
     */
    private void clearFileCache(Long fileId) {
        String cacheKey = CACHE_FILE_KEY + "id:" + fileId;
        redisCache.deleteObject(cacheKey);
    }

    /**
     * 计算文件MD5
     *
     * @param file 文件
     * @return MD5值
     */
    private String calculateMd5(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return DigestUtils.md5DigestAsHex(inputStream);
        } catch (IOException e) {
            log.error("计算文件MD5失败", e);
            return null;
        }
    }

    /**
     * 转换为VO
     *
     * @param sysFile 文件实体
     * @return 文件VO
     */
    private SysFileVO convertToVO(SysFile sysFile) {
        if (sysFile == null) {
            return null;
        }

        SysFileVO vo = new SysFileVO();
        BeanUtils.copyProperties(sysFile, vo);

        // 设置文件大小格式化
        vo.setFileSizeFormat(formatFileSize(sysFile.getFileSize()));

        // 获取存储策略
        StorageType type = StorageType.values()[sysFile.getStorageType()];
        FileStorage storage = fileStorageFactory.getFileStorage(type);
        
        // 设置文件URL
        vo.setUrl(storage.getFileUrl(sysFile.getFilePath()));

        // 设置状态名称
        vo.setStatusName(sysFile.getStatus() == 0 ? "正常" : "禁用");
        
        // 设置存储类型名称
        String[] storageTypeNames = {"本地存储", "阿里云OSS", "腾讯云COS", "七牛云Kodo", "MinIO"};
        vo.setStorageTypeName(storageTypeNames[sysFile.getStorageType()]);

        return vo;
    }

    /**
     * 格式化文件大小
     *
     * @param size 文件大小（字节）
     * @return 格式化后的文件大小
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2fKB", (double) size / 1024);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2fMB", (double) size / (1024 * 1024));
        } else {
            return String.format("%.2fGB", (double) size / (1024 * 1024 * 1024));
        }
    }
} 