package cn.oalo.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品VO
 */
@Data
@Schema(name = "产品VO", description = "产品数据视图对象")
public class SysProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @Schema(description = "产品ID")
    private Long productId;

    /**
     * 产品名称
     */
    @Schema(description = "产品名称")
    private String productName;

    /**
     * 产品编码
     */
    @Schema(description = "产品编码")
    private String productCode;

    /**
     * 产品分类
     */
    @Schema(description = "产品分类")
    private String category;

    /**
     * 产品价格
     */
    @Schema(description = "产品价格")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @Schema(description = "库存数量")
    private Integer stock;

    /**
     * 产品描述
     */
    @Schema(description = "产品描述")
    private String description;

    /**
     * 产品图片
     */
    @Schema(description = "产品图片")
    private String image;

    /**
     * 状态(0:下架 1:上架)
     */
    @Schema(description = "状态(0:下架 1:上架)")
    private Integer status;

    /**
     * 状态名称
     */
    @Schema(description = "状态名称")
    private String statusName;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
} 