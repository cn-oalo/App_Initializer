package cn.oalo.system.service;

import cn.oalo.system.entity.SysFile;
import cn.oalo.system.vo.SysFileVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件服务接口
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件信息
     * @throws IOException IO异常
     */
    SysFileVO upload(MultipartFile file) throws IOException;

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @return 文件信息列表
     * @throws IOException IO异常
     */
    List<SysFileVO> batchUpload(List<MultipartFile> files) throws IOException;

    /**
     * 下载文件
     *
     * @param fileId   文件ID
     * @param response 响应对象
     * @throws IOException IO异常
     */
    void download(Long fileId, HttpServletResponse response) throws IOException;

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 是否删除成功
     */
    boolean deleteFile(Long fileId);

    /**
     * 批量删除文件
     *
     * @param fileIds 文件ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteFile(List<Long> fileIds);

    /**
     * 获取文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    SysFileVO getFileInfo(Long fileId);

    /**
     * 根据文件名查询文件
     *
     * @param fileName 文件名
     * @return 文件列表
     */
    List<SysFileVO> listFilesByName(String fileName);
} 