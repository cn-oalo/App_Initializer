package cn.oalo.framework.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件存储策略接口
 */
public interface FileStorage {

    /**
     * 上传文件
     *
     * @param file     文件对象
     * @param fileName 文件名
     * @return 文件访问路径
     * @throws IOException IO异常
     */
    String upload(MultipartFile file, String fileName) throws IOException;

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param fileName    文件名
     * @return 文件访问路径
     * @throws IOException IO异常
     */
    String upload(InputStream inputStream, String fileName) throws IOException;

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    boolean delete(String filePath);

    /**
     * 获取文件访问URL
     *
     * @param filePath 文件路径
     * @return 文件访问URL
     */
    String getFileUrl(String filePath);

    /**
     * 获取文件下载流
     *
     * @param filePath 文件路径
     * @return 文件输入流
     * @throws IOException IO异常
     */
    InputStream getFileStream(String filePath) throws IOException;

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    StorageType getType();
} 