package cn.oalo.common.util;

/**
 * MIME类型工具类
 */
public class MimeTypeUtils {
    /**
     * 默认允许的文件类型
     */
    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            // 文本
            "txt", "pdf",
            // 压缩文件
            "zip", "rar", "gz", "7z",
            // 音视频
            "mp3", "mp4", "avi", "rmvb"
    };

    /**
     * 默认的文件类型映射
     */
    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};
    public static final String[] DOCUMENT_EXTENSION = {"doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "txt"};
    public static final String[] COMPRESS_EXTENSION = {"zip", "rar", "gz", "7z"};
    public static final String[] MEDIA_EXTENSION = {"mp3", "mp4", "avi", "rmvb"};

    /**
     * 默认最大文件大小 10M
     */
    public static final long DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    /**
     * 根据文件类型获取MIME类型
     *
     * @param type 文件类型
     * @return MIME类型
     */
    public static String getMimeType(String type) {
        if (type == null || type.isEmpty()) {
            return "application/octet-stream";
        }
        type = type.toLowerCase();
        switch (type) {
            case "bmp":
                return "image/bmp";
            case "gif":
                return "image/gif";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/x-rar-compressed";
            case "gz":
                return "application/gzip";
            case "7z":
                return "application/x-7z-compressed";
            case "mp3":
                return "audio/mpeg";
            case "mp4":
                return "video/mp4";
            case "avi":
                return "video/x-msvideo";
            case "rmvb":
                return "application/vnd.rn-realmedia-vbr";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 根据MIME类型获取文件扩展名
     *
     * @param mimeType MIME类型
     * @return 文件扩展名
     */
    public static String getExtension(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            return "";
        }
        mimeType = mimeType.toLowerCase();
        switch (mimeType) {
            case "image/bmp":
                return "bmp";
            case "image/gif":
                return "gif";
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "application/msword":
                return "doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return "docx";
            case "application/vnd.ms-excel":
                return "xls";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return "xlsx";
            case "application/vnd.ms-powerpoint":
                return "ppt";
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return "pptx";
            case "application/pdf":
                return "pdf";
            case "text/plain":
                return "txt";
            case "application/zip":
                return "zip";
            case "application/x-rar-compressed":
                return "rar";
            case "application/gzip":
                return "gz";
            case "application/x-7z-compressed":
                return "7z";
            case "audio/mpeg":
                return "mp3";
            case "video/mp4":
                return "mp4";
            case "video/x-msvideo":
                return "avi";
            case "application/vnd.rn-realmedia-vbr":
                return "rmvb";
            default:
                return "";
        }
    }
} 