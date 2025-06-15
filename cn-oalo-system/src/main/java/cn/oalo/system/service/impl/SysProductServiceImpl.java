package cn.oalo.system.service.impl;

import cn.oalo.framework.redis.RedisCache;
import cn.oalo.system.entity.SysProduct;
import cn.oalo.system.mapper.SysProductMapper;
import cn.oalo.system.service.SysProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 产品服务实现类
 */
@Service
public class SysProductServiceImpl extends ServiceImpl<SysProductMapper, SysProduct> implements SysProductService {

    @Autowired
    private SysProductMapper productMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 产品缓存键前缀
     */
    private static final String CACHE_PRODUCT_KEY = "sys:product:";

    @Override
    public SysProduct getProductById(Long productId) {
        // 先从缓存中获取
        String cacheKey = CACHE_PRODUCT_KEY + "id:" + productId;
        SysProduct product = redisCache.getCacheObject(cacheKey);
        if (product != null) {
            return product;
        }
        
        // 缓存中没有，从数据库查询
        product = productMapper.selectProductById(productId);
        if (product != null) {
            // 放入缓存，设置过期时间为1小时
            redisCache.setCacheObject(cacheKey, product, 1, TimeUnit.HOURS);
        }
        return product;
    }

    @Override
    public SysProduct getProductByCode(String productCode) {
        // 先从缓存中获取
        String cacheKey = CACHE_PRODUCT_KEY + "code:" + productCode;
        SysProduct product = redisCache.getCacheObject(cacheKey);
        if (product != null) {
            return product;
        }
        
        // 缓存中没有，从数据库查询
        product = productMapper.selectProductByCode(productCode);
        if (product != null) {
            // 放入缓存，设置过期时间为1小时
            redisCache.setCacheObject(cacheKey, product, 1, TimeUnit.HOURS);
        }
        return product;
    }

    @Override
    public List<SysProduct> getProductsByCategory(String category) {
        // 先从缓存中获取
        String cacheKey = CACHE_PRODUCT_KEY + "category:" + category;
        List<SysProduct> products = redisCache.getCacheList(cacheKey);
        if (products != null && !products.isEmpty()) {
            return products;
        }
        
        // 缓存中没有，从数据库查询
        products = productMapper.selectProductsByCategory(category);
        if (products != null && !products.isEmpty()) {
            // 放入缓存，设置过期时间为1小时
            redisCache.setCacheList(cacheKey, products);
            redisCache.expire(cacheKey, 1, TimeUnit.HOURS);
        }
        return products;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProduct(SysProduct product) {
        // 设置默认值
        product.setStatus(1);
        product.setDelFlag(0);
        product.setCreateTime(LocalDateTime.now());
        
        // 保存到数据库
        boolean result = save(product);
        
        // 清除相关缓存
        if (result) {
            clearProductCache(product);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(SysProduct product) {
        // 设置更新时间
        product.setUpdateTime(LocalDateTime.now());
        
        // 更新数据库
        boolean result = updateById(product);
        
        // 清除相关缓存
        if (result) {
            clearProductCache(product);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProductById(Long productId) {
        // 查询产品信息
        SysProduct product = getProductById(productId);
        if (product == null) {
            return false;
        }
        
        // 逻辑删除
        product.setDelFlag(1);
        product.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(product);
        
        // 清除相关缓存
        if (result) {
            clearProductCache(product);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProductByIds(Long[] productIds) {
        // 批量逻辑删除
        SysProduct updateEntity = new SysProduct();
        updateEntity.setDelFlag(1);
        updateEntity.setUpdateTime(LocalDateTime.now());
        
        boolean result = update(updateEntity, new LambdaQueryWrapper<SysProduct>()
                .in(SysProduct::getProductId, Arrays.asList(productIds)));
        
        // 清除相关缓存
        if (result) {
            for (Long productId : productIds) {
                redisCache.deleteObject(CACHE_PRODUCT_KEY + "id:" + productId);
            }
        }
        
        return result;
    }
    
    /**
     * 清除产品相关缓存
     *
     * @param product 产品信息
     */
    private void clearProductCache(SysProduct product) {
        redisCache.deleteObject(CACHE_PRODUCT_KEY + "id:" + product.getProductId());
        if (product.getProductCode() != null) {
            redisCache.deleteObject(CACHE_PRODUCT_KEY + "code:" + product.getProductCode());
        }
        if (product.getCategory() != null) {
            redisCache.deleteObject(CACHE_PRODUCT_KEY + "category:" + product.getCategory());
        }
    }
} 