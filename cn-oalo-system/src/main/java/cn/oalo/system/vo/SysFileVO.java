package cn.oalo.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件VO
 */
@Data
@ApiModel(value = "文件VO")
public class SysFileVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @ApiModelProperty(value = "文件ID")
    private Long fileId;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 原始文件名
     */
    @ApiModelProperty(value = "原始文件名")
    private String originalName;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    /**
     * 文件大小（字节）
     */
    @ApiModelProperty(value = "文件大小（字节）")
    private Long fileSize;

    /**
     * 文件大小（格式化）
     */
    @ApiModelProperty(value = "文件大小（格式化）")
    private String fileSizeFormat;

    /**
     * 文件URL
     */
    @ApiModelProperty(value = "文件URL")
    private String url;

    /**
     * 存储类型（0本地 1阿里云OSS 2腾讯云COS 3七牛云Kodo 4MinIO）
     */
    @ApiModelProperty(value = "存储类型（0本地 1阿里云OSS 2腾讯云COS 3七牛云Kodo 4MinIO）")
    private Integer storageType;

    /**
     * 存储类型名称
     */
    @ApiModelProperty(value = "存储类型名称")
    private String storageTypeName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 状态（0正常 1禁用）
     */
    @ApiModelProperty(value = "状态（0正常 1禁用）")
    private Integer status;

    /**
     * 状态名称
     */
    @ApiModelProperty(value = "状态名称")
    private String statusName;
} 