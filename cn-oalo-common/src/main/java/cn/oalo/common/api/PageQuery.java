package cn.oalo.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数
 */
@Data
@Schema(name = "分页查询参数", description = "分页查询参数")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量", example = "10")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String orderBy;

    /**
     * 排序方式：asc/desc
     */
    @Schema(description = "排序方式：asc/desc")
    private String orderType;
} 