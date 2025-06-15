package cn.oalo.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品DTO
 */
@Data
@Schema(name = "产品DTO", description = "产品数据传输对象")
public class SysProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @Schema(description = "产品ID")
    private Long productId;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productName;

    /**
     * 产品编码
     */
    @NotBlank(message = "产品编码不能为空")
    @Schema(description = "产品编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productCode;

    /**
     * 产品分类
     */
    @NotBlank(message = "产品分类不能为空")
    @Schema(description = "产品分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    /**
     * 产品价格
     */
    @NotNull(message = "产品价格不能为空")
    @DecimalMin(value = "0.01", message = "产品价格必须大于0")
    @Schema(description = "产品价格", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    /**
     * 库存数量
     */
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能小于0")
    @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED)
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
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
} 