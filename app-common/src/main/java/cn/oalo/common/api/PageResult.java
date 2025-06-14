package cn.oalo.common.api;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 分页数据封装类
 */
@Data
@AllArgsConstructor
@ApiModel(value = "分页结果", description = "分页结果")
public class PageResult<T> {

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Long total;
    
    /**
     * 当前页数据
     */
    @ApiModelProperty(value = "当前页数据")
    private List<T> list;
    
    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private Long pages;
    
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;
    
    /**
     * 每页记录数
     */
    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize;

    /**
     * 创建分页结果对象
     * 
     * @param list 当前页数据列表
     * @param total 总记录数
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @param <T> 列表元素类型
     * @return 分页结果对象
     */
    public static <T> PageResult<T> of(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        Long pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        return new PageResult<>(total, list, pages, pageNum, pageSize);
    }
    
    /**
     * 根据查询参数创建分页结果对象
     * 
     * @param list 当前页数据列表
     * @param total 总记录数
     * @param query 查询参数
     * @param <T> 列表元素类型
     * @return 分页结果对象
     */
    public static <T> PageResult<T> of(List<T> list, Long total, PageQuery query) {
        return of(list, total, query.getPageNum(), query.getPageSize());
    }
} 