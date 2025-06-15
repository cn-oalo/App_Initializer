package cn.oalo.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件VO
 */
@Data
@Schema(name = "文件VO", description = "文件数据视图对象")
public class SysFileVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @Schema(description = "文件ID")
    private Long fileId;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    private String fileName;

    /**
     * 原始文件名
     */
    @Schema(description = "原始文件名")
    private String originalName;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String fileType;

    /**
     * 文件大小（字节）
     */
    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    /**
     * 文件大小（格式化）
     */
    @Schema(description = "文件大小（格式化）")
    private String fileSizeFormat;

    /**
     * 文件URL
     */
    @Schema(description = "文件URL")
    private String url;

    /**
     * 存储类型（0本地 1阿里云OSS 2腾讯云COS 3七牛云Kodo 4MinIO）
     */
    @Schema(description = "存储类型（0本地 1阿里云OSS 2腾讯云COS 3七牛云Kodo 4MinIO）")
    private Integer storageType;

    /**
     * 存储类型名称
     */
    @Schema(description = "存储类型名称")
    private String storageTypeName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 状态（0正常 1禁用）
     */
    @Schema(description = "状态（0正常 1禁用）")
    private Integer status;

    /**
     * 状态名称
     */
    @Schema(description = "状态名称")
    private String statusName;
} 