package cn.oalo.common.api;

import javax.validation.constraints.Min;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询参数
 */
@Data
@ApiModel(value = "分页查询参数", description = "分页查询参数")
public class PageQuery {

    /**
     * 页码，从1开始
     */
    @ApiModelProperty(value = "页码，从1开始", required = true, example = "1")
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    @ApiModelProperty(value = "每页记录数", required = true, example = "10")
    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;
    
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段", example = "id")
    private String orderBy;
    
    /**
     * 是否升序
     */
    @ApiModelProperty(value = "是否升序", example = "true")
    private Boolean isAsc = true;
    
    /**
     * 计算当前页的起始索引
     * 
     * @return 起始索引
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
} 