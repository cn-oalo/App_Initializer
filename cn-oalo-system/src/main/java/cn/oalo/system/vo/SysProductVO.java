package cn.oalo.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品VO
 */
@Data
@ApiModel(value = "产品VO")
public class SysProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private Long productId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String productCode;

    /**
     * 产品分类
     */
    @ApiModelProperty(value = "产品分类")
    private String category;

    /**
     * 产品价格
     */
    @ApiModelProperty(value = "产品价格")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Integer stock;

    /**
     * 产品描述
     */
    @ApiModelProperty(value = "产品描述")
    private String description;

    /**
     * 产品图片
     */
    @ApiModelProperty(value = "产品图片")
    private String image;

    /**
     * 状态(0:下架 1:上架)
     */
    @ApiModelProperty(value = "状态(0:下架 1:上架)")
    private Integer status;

    /**
     * 状态名称
     */
    @ApiModelProperty(value = "状态名称")
    private String statusName;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
} 