package cn.oalo.interfaces.controller;

import cn.oalo.common.api.R;
import cn.oalo.infrastructure.oss.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件控制器
 */
@Slf4j
@Api(tags = "文件接口")
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class OssController {
    
    private final OssService ossService;
    
    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件URL
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public R<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.failed("上传文件不能为空");
        }
        
        String originalFilename = file.getOriginalFilename();
        String url = ossService.upload(file, originalFilename);
        
        Map<String, String> result = new HashMap<>(4);
        result.put("url", url);
        result.put("filename", originalFilename);
        result.put("size", String.valueOf(file.getSize()));
        
        return R.ok(result);
    }
    
    /**
     * 删除文件
     *
     * @param url 文件URL
     * @return 操作结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除文件")
    @ApiImplicitParam(name = "url", value = "文件URL", required = true, dataType = "String", paramType = "query")
    public R<Boolean> delete(@RequestParam("url") String url) {
        boolean result = ossService.delete(url);
        return R.ok(result);
    }
    
    /**
     * 下载文件
     *
     * @param url 文件URL
     * @return 文件流
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载文件")
    @ApiImplicitParam(name = "url", value = "文件URL", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Object> download(@RequestParam("url") String url) {
        try {
            // 获取文件名
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            // 获取文件流
            InputStream inputStream = ossService.download(url);
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String encodeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodeFileName);
            
            return new ResponseEntity<>(inputStream.readAllBytes(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage());
            return new ResponseEntity<>("文件下载失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 