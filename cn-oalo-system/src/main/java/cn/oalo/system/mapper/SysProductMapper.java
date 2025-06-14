package cn.oalo.system.mapper;

import cn.oalo.system.entity.SysProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品Mapper接口
 */
@Mapper
public interface SysProductMapper extends BaseMapper<SysProduct> {

    /**
     * 根据产品ID查询产品
     *
     * @param productId 产品ID
     * @return 产品信息
     */
    SysProduct selectProductById(@Param("productId") Long productId);

    /**
     * 根据产品编码查询产品
     *
     * @param productCode 产品编码
     * @return 产品信息
     */
    SysProduct selectProductByCode(@Param("productCode") String productCode);

    /**
     * 根据分类查询产品列表
     *
     * @param category 产品分类
     * @return 产品列表
     */
    List<SysProduct> selectProductsByCategory(@Param("category") String category);
} 