package cn.oalo.common.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数
 */
@Data
@ApiModel(value = "分页查询参数")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码", example = "1")
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String orderBy;

    /**
     * 排序方式：asc/desc
     */
    @ApiModelProperty(value = "排序方式：asc/desc")
    private String orderType;
} 