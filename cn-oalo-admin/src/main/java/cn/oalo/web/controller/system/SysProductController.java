package cn.oalo.web.controller.system;

import cn.oalo.common.api.PageResult;
import cn.oalo.common.api.R;
import cn.oalo.system.dto.SysProductDTO;
import cn.oalo.system.entity.SysProduct;
import cn.oalo.system.service.SysProductService;
import cn.oalo.system.vo.SysProductVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品管理控制器
 */
@Api(tags = "产品管理接口")
@RestController
@RequestMapping("/system/product")
public class SysProductController {

    @Autowired
    private SysProductService productService;

    /**
     * 获取产品列表
     */
    @ApiOperation("获取产品列表")
    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @GetMapping("/list")
    public R<PageResult<SysProductVO>> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("产品名称") @RequestParam(required = false) String productName,
            @ApiParam("产品编码") @RequestParam(required = false) String productCode,
            @ApiParam("产品分类") @RequestParam(required = false) String category) {
        
        // 构建查询条件
        LambdaQueryWrapper<SysProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(productName), SysProduct::getProductName, productName)
                .like(StringUtils.hasText(productCode), SysProduct::getProductCode, productCode)
                .eq(StringUtils.hasText(category), SysProduct::getCategory, category)
                .eq(SysProduct::getDelFlag, 0)
                .orderByDesc(SysProduct::getCreateTime);
        
        // 分页查询
        IPage<SysProduct> page = productService.page(new Page<>(pageNum, pageSize), queryWrapper);
        
        // 转换为VO
        List<SysProductVO> voList = convertToVOList(page.getRecords());
        
        // 返回结果
        return R.ok(new PageResult<>(pageNum, pageSize, page.getTotal(), voList));
    }

    /**
     * 获取产品详情
     */
    @ApiOperation("获取产品详情")
    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @GetMapping("/{productId}")
    public R<SysProductVO> getInfo(@PathVariable Long productId) {
        SysProduct product = productService.getProductById(productId);
        if (product == null) {
            return R.failed("产品不存在");
        }
        return R.ok(convertToVO(product));
    }

    /**
     * 新增产品
     */
    @ApiOperation("新增产品")
    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysProductDTO productDTO) {
        // 检查产品编码是否已存在
        SysProduct existProduct = productService.getProductByCode(productDTO.getProductCode());
        if (existProduct != null) {
            return R.failed("产品编码已存在");
        }
        
        // 转换为实体
        SysProduct product = new SysProduct();
        BeanUtils.copyProperties(productDTO, product);
        
        // 保存产品
        boolean result = productService.addProduct(product);
        return result ? R.ok() : R.failed("新增产品失败");
    }

    /**
     * 修改产品
     */
    @ApiOperation("修改产品")
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysProductDTO productDTO) {
        if (productDTO.getProductId() == null) {
            return R.failed("产品ID不能为空");
        }
        
        // 检查产品是否存在
        SysProduct existProduct = productService.getProductById(productDTO.getProductId());
        if (existProduct == null) {
            return R.failed("产品不存在");
        }
        
        // 检查产品编码是否已被其他产品使用
        if (!existProduct.getProductCode().equals(productDTO.getProductCode())) {
            SysProduct codeExistProduct = productService.getProductByCode(productDTO.getProductCode());
            if (codeExistProduct != null) {
                return R.failed("产品编码已存在");
            }
        }
        
        // 转换为实体
        SysProduct product = new SysProduct();
        BeanUtils.copyProperties(productDTO, product);
        
        // 更新产品
        boolean result = productService.updateProduct(product);
        return result ? R.ok() : R.failed("修改产品失败");
    }

    /**
     * 删除产品
     */
    @ApiOperation("删除产品")
    @PreAuthorize("@ss.hasPermi('system:product:remove')")
    @DeleteMapping("/{productIds}")
    public R<Void> remove(@PathVariable Long[] productIds) {
        boolean result = productService.deleteProductByIds(productIds);
        return result ? R.ok() : R.failed("删除产品失败");
    }

    /**
     * 获取产品分类列表
     */
    @ApiOperation("获取产品分类列表")
    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @GetMapping("/categories")
    public R<List<String>> getCategories() {
        // 查询所有分类
        LambdaQueryWrapper<SysProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysProduct::getCategory)
                .groupBy(SysProduct::getCategory)
                .eq(SysProduct::getDelFlag, 0);
        
        List<SysProduct> products = productService.list(queryWrapper);
        List<String> categories = products.stream()
                .map(SysProduct::getCategory)
                .distinct()
                .collect(Collectors.toList());
        
        return R.ok(categories);
    }

    /**
     * 更新产品状态
     */
    @ApiOperation("更新产品状态")
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @PutMapping("/status/{productId}/{status}")
    public R<Void> updateStatus(
            @PathVariable Long productId,
            @PathVariable Integer status) {
        
        if (status != 0 && status != 1) {
            return R.failed("状态值无效");
        }
        
        SysProduct product = new SysProduct();
        product.setProductId(productId);
        product.setStatus(status);
        
        boolean result = productService.updateProduct(product);
        return result ? R.ok() : R.failed("更新产品状态失败");
    }

    /**
     * 转换为VO
     */
    private SysProductVO convertToVO(SysProduct product) {
        if (product == null) {
            return null;
        }
        
        SysProductVO vo = new SysProductVO();
        BeanUtils.copyProperties(product, vo);
        
        // 设置状态名称
        vo.setStatusName(product.getStatus() != null && product.getStatus() == 1 ? "上架" : "下架");
        
        return vo;
    }

    /**
     * 转换为VO列表
     */
    private List<SysProductVO> convertToVOList(List<SysProduct> products) {
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }
        
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
} 