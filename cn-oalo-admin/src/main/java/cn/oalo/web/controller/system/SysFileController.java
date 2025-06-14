package cn.oalo.web.controller.system;

import cn.oalo.common.api.R;
import cn.oalo.common.api.ResultCode;
import cn.oalo.framework.storage.FileStorage;
import cn.oalo.framework.storage.FileStorageFactory;
import cn.oalo.framework.storage.StorageType;
import cn.oalo.system.service.SysFileService;
import cn.oalo.system.vo.SysFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件控制器
 */
@Api(tags = "文件管理")
@RestController
@RequestMapping("/system/file")
public class SysFileController {

    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private SysFileService fileService;
    
    @Autowired
    private FileStorageFactory fileStorageFactory;

    /**
     * 上传文件
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public R<SysFileVO> upload(@RequestParam("file") MultipartFile file) {
        try {
            SysFileVO fileInfo = fileService.upload(file);
            return R.success(fileInfo);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return R.failed(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 批量上传文件
     */
    @ApiOperation("批量上传文件")
    @PostMapping("/batchUpload")
    public R<List<SysFileVO>> batchUpload(@RequestParam("files") MultipartFile[] files) {
        try {
            List<MultipartFile> fileList = Arrays.asList(files);
            List<SysFileVO> fileInfos = fileService.batchUpload(fileList);
            return R.success(fileInfos);
        } catch (IOException e) {
            log.error("批量上传文件失败", e);
            return R.failed(ResultCode.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 下载文件
     */
    @ApiOperation("下载文件")
    @ApiImplicitParam(name = "fileId", value = "文件ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable Long fileId, HttpServletResponse response) {
        try {
            fileService.download(fileId, response);
        } catch (IOException e) {
            log.error("文件下载失败", e);
        }
    }
    
    /**
     * 根据文件路径下载文件
     */
    @ApiOperation("根据文件路径下载文件")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "filePath", value = "文件路径", required = true, dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "storageType", value = "存储类型（0本地 1阿里云OSS 2腾讯云COS 3七牛云Kodo 4MinIO）", required = false, dataType = "Integer", paramType = "query", defaultValue = "0")
    })
    @GetMapping("/download")
    public void downloadByPath(@RequestParam("filePath") String filePath, 
                              @RequestParam(value = "storageType", defaultValue = "0") Integer storageTypeValue,
                              HttpServletResponse response) {
        try {
            StorageType storageType = StorageType.values()[storageTypeValue];
            FileStorage storage = fileStorageFactory.getFileStorage(storageType);
            
            // 获取文件流
            InputStream inputStream = storage.getFileStream(filePath);
            
            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            
            // 写入响应
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (IOException e) {
            log.error("文件下载失败", e);
        }
    }

    /**
     * 删除文件
     */
    @ApiOperation("删除文件")
    @ApiImplicitParam(name = "fileId", value = "文件ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/{fileId}")
    public R<Boolean> delete(@PathVariable Long fileId) {
        boolean result = fileService.deleteFile(fileId);
        return result ? R.success(true) : R.failed(ResultCode.FILE_DELETE_ERROR);
    }

    /**
     * 批量删除文件
     */
    @ApiOperation("批量删除文件")
    @ApiImplicitParam(name = "fileIds", value = "文件ID列表", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/batch")
    public R<Boolean> batchDelete(@RequestParam("fileIds") String fileIds) {
        List<Long> ids = Arrays.stream(fileIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        boolean result = fileService.batchDeleteFile(ids);
        return result ? R.success(true) : R.failed(ResultCode.FILE_DELETE_ERROR);
    }

    /**
     * 获取文件信息
     */
    @ApiOperation("获取文件信息")
    @ApiImplicitParam(name = "fileId", value = "文件ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/{fileId}")
    public R<SysFileVO> getFileInfo(@PathVariable Long fileId) {
        SysFileVO fileInfo = fileService.getFileInfo(fileId);
        return R.success(fileInfo);
    }

    /**
     * 查询文件列表
     */
    @ApiOperation("查询文件列表")
    @ApiImplicitParam(name = "fileName", value = "文件名", dataType = "String", paramType = "query")
    @GetMapping("/list")
    public R<List<SysFileVO>> list(@RequestParam(value = "fileName", required = false) String fileName) {
        List<SysFileVO> fileList = fileService.listFilesByName(fileName);
        return R.success(fileList);
    }
} 