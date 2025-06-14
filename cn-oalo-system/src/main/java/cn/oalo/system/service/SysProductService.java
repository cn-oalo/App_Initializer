package cn.oalo.system.service;

import cn.oalo.system.entity.SysProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 产品服务接口
 */
public interface SysProductService extends IService<SysProduct> {

    /**
     * 根据产品ID查询产品
     *
     * @param productId 产品ID
     * @return 产品信息
     */
    SysProduct getProductById(Long productId);

    /**
     * 根据产品编码查询产品
     *
     * @param productCode 产品编码
     * @return 产品信息
     */
    SysProduct getProductByCode(String productCode);

    /**
     * 根据分类查询产品列表
     *
     * @param category 产品分类
     * @return 产品列表
     */
    List<SysProduct> getProductsByCategory(String category);

    /**
     * 新增产品
     *
     * @param product 产品信息
     * @return 结果
     */
    boolean addProduct(SysProduct product);

    /**
     * 修改产品
     *
     * @param product 产品信息
     * @return 结果
     */
    boolean updateProduct(SysProduct product);

    /**
     * 删除产品
     *
     * @param productId 产品ID
     * @return 结果
     */
    boolean deleteProductById(Long productId);

    /**
     * 批量删除产品
     *
     * @param productIds 需要删除的产品ID数组
     * @return 结果
     */
    boolean deleteProductByIds(Long[] productIds);
} 