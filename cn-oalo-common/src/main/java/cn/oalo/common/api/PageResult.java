package cn.oalo.common.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
@Data
@ApiModel(value = "分页结果")
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;

    /**
     * 每页数量
     */
    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Long total;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private Integer pages;

    /**
     * 数据列表
     */
    @ApiModelProperty(value = "数据列表")
    private List<T> list;

    /**
     * 构造方法
     *
     * @param pageNum  当前页码
     * @param pageSize 每页数量
     * @param total    总记录数
     * @param list     数据列表
     */
    public PageResult(Integer pageNum, Integer pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
        this.pages = (int) Math.ceil((double) total / pageSize);
    }
} 