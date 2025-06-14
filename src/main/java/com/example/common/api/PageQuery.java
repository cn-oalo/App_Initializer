package com.example.common.api;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页查询参数
 */
@Data
public class PageQuery {

    /**
     * 页码，从1开始
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;
    
    /**
     * 排序字段
     */
    private String orderBy;
    
    /**
     * 是否升序
     */
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