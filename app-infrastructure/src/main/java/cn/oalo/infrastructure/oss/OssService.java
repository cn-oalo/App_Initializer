package cn.oalo.infrastructure.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * OSS服务接口
 */
public interface OssService {
    
    /**
     * 上传文件
     *
     * @param file     文件
     * @param fileName 文件名
     * @return 文件URL
     */
    String upload(MultipartFile file, String fileName);
    
    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param fileName    文件名
     * @return 文件URL
     */
    String upload(InputStream inputStream, String fileName);
    
    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 是否成功
     */
    boolean delete(String fileName);
    
    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @return 文件流
     */
    InputStream download(String fileName);
    
    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @return 文件URL
     */
    String getUrl(String fileName);
} 