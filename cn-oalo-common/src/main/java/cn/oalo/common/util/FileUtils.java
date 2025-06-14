package cn.oalo.common.util;

import cn.oalo.common.exception.BusinessException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件处理工具类
 */
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = "upload";

    /**
     * 设置默认上传的地址
     *
     * @param defaultBaseDir 默认上传的地址
     */
    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUtils.defaultBaseDir = defaultBaseDir;
    }

    /**
     * 获取默认上传的地址
     *
     * @return 默认上传的地址
     */
    public static String getDefaultBaseDir() {
        return defaultBaseDir;
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static String upload(String baseDir, MultipartFile file) throws IOException {
        try {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 允许的文件类型
     * @return 返回上传成功的文件名
     * @throws IOException 比如读写文件出错时
     */
    public static String upload(String baseDir, MultipartFile file, String[] allowedExtension) throws IOException {
        // 检查文件大小
        assertAllowed(file, allowedExtension);

        String fileName = extractFilename(file);
        String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();

        File desc = new File(absPath);
        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        file.transferTo(desc);
        return getPathFileName(baseDir, fileName);
    }

    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = DateUtils.datePath() + "/" + UUID.randomUUID().toString().replaceAll("-", "") + "." + extension;
        return fileName;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    /**
     * 文件下载
     *
     * @param filePath 文件路径
     * @param response 响应对象
     * @throws IOException IO异常
     */
    public static void download(String filePath, HttpServletResponse response) throws IOException {
        download(filePath, response, null);
    }

    /**
     * 文件下载
     *
     * @param filePath 文件路径
     * @param response 响应对象
     * @param filename 指定下载的文件名
     * @throws IOException IO异常
     */
    public static void download(String filePath, HttpServletResponse response, String filename) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new BusinessException("文件不存在");
        }

        if (StringUtils.isBlank(filename)) {
            filename = file.getName();
        }

        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error("下载文件失败", e);
            throw new BusinessException("下载文件失败");
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean delete(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param file             上传的文件
     * @param allowedExtension 允许的文件类型
     * @return true/false
     */
    public static boolean isAllowedExtension(MultipartFile file, String[] allowedExtension) {
        String extension = getExtension(file);
        if (allowedExtension == null || allowedExtension.length == 0) {
            return true;
        }
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查文件是否可以上传
     *
     * @param file             上传的文件
     * @param allowedExtension 文件类型
     */
    public static void assertAllowed(MultipartFile file, String[] allowedExtension) {
        if (file == null) {
            throw new BusinessException("文件不能为空");
        }
        if (file.getSize() > MimeTypeUtils.DEFAULT_MAX_SIZE) {
            throw new BusinessException("文件大小超过限制");
        }
        if (!isAllowedExtension(file, allowedExtension)) {
            throw new BusinessException("文件类型不支持");
        }
    }

    /**
     * 获取文件的绝对路径
     *
     * @param uploadDir 上传目录
     * @param fileName  文件名
     * @return 绝对路径
     * @throws IOException IO异常
     */
    private static File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);
        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    /**
     * 获取文件路径
     *
     * @param uploadDir 上传目录
     * @param fileName  文件名
     * @return 文件路径
     */
    private static String getPathFileName(String uploadDir, String fileName) {
        return uploadDir + File.separator + fileName;
    }

    /**
     * 将文本内容写入文件
     *
     * @param content  文本内容
     * @param filePath 文件路径
     * @throws IOException IO异常
     */
    public static void writeStringToFile(String content, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }

    /**
     * 从文件中读取文本内容
     *
     * @param filePath 文件路径
     * @return 文本内容
     * @throws IOException IO异常
     */
    public static String readFileToString(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new BusinessException("文件不存在");
        }
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    /**
     * 日期工具类
     */
    private static class DateUtils {
        /**
         * 获取当前日期路径
         *
         * @return 日期路径
         */
        public static String datePath() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(new Date());
        }
    }
} 