# 企业级应用框架

一个基于 Spring Boot 的企业级应用框架，采用 DDD 分层架构。

## 技术选型

- JDK 1.8+
- Spring Boot 2.7.14
- MyBatis-Plus 3.5.3
- MySQL 5.7+
- Redis
- Aliyun OSS
- JWT
- Swagger + Knife4j

## 项目结构

本项目采用 DDD 分层架构，分为以下六个模块：

- **app-common**: 公共模块，包含工具类、异常、常量等
- **app-domain**: 领域模块，包含领域实体、值对象、领域服务等
- **app-infrastructure**: 基础设施模块，包含数据访问、消息队列、缓存等
- **app-application**: 应用服务模块，包含应用服务、DTO等
- **app-interfaces**: 接口模块，包含控制器、VO等
- **app-boot**: 启动模块，包含应用启动类、配置等

## 功能特性

- 统一响应格式和全局异常处理
- 基于JWT的认证和授权
- 多数据源支持
- 防止重复提交
- 自动填充创建时间、修改时间、创建人、修改人
- OSS文件上传下载
- 国际化支持
- ThreadLocal用户上下文
- 序列化工具

## 快速开始

### 前置条件

- JDK 1.8 或更高版本
- Maven 3.6 或更高版本
- MySQL 5.7 或更高版本

### 运行步骤

1. 克隆项目到本地
2. 导入数据库脚本，创建 `enterprise_app` 和 `enterprise_app_slave` 数据库
3. 修改 `app-boot/src/main/resources/application.yml` 配置文件中的数据库连接信息
4. 修改 `app-boot/src/main/resources/application.yml` 配置文件中的阿里云OSS配置
5. 运行 `app-boot` 模块中的 `AppApplication` 启动类
6. 访问 `http://localhost:8080/api/doc.html` 查看接口文档

### 默认用户

- 用户名：admin
- 密码：admin123

## 开发指南

## 依赖添加流程说明：

### 根据依赖用途确定添加位置：
- 通用工具类依赖：添加到 app-common 模块
- 领域相关依赖：添加到 app-domain 模块
- 基础设施依赖(数据库、消息队列等)：添加到 app-infrastructure 模块
- 应用服务依赖：添加到 app-application 模块
- 接口相关依赖(如Swagger)：添加到 app-interfaces 模块
- 启动相关依赖：添加到 app-boot 模块
### 添加依赖的方式：
- 直接在对应模块的pom.xml中添加
- 优先在父pom中声明依赖版本，各模块引用时不指定版本
- 依赖传递规则：
- 下层模块不应依赖上层模块(app-common → app-domain → app-infrastructure → app-application → app-interfaces → app-boot)
- 如果多个模块需要，放在更底层的模块中

## 拓展新模块流程

如果需要新增业务模块，请按照以下步骤进行：

1. **在领域模块（app-domain）中添加实体类**

   ```java
   package cn.oalo.domain.entity;
   
   import lombok.Data;
   
   @Data
   public class Product extends BaseEntity {
       
       private Long id;
       
       private String name;
       
       private String description;
       
       private BigDecimal price;
       
       private Integer stock;
   }
   ```

2. **在领域模块中添加仓库接口**

   ```java
   package cn.oalo.domain.repository;
   
   import cn.oalo.domain.entity.Product;
   import java.util.List;
   
   public interface ProductRepository {
       
       /**
        * 保存产品
        */
       Product save(Product product);
       
       /**
        * 根据ID查询产品
        */
       Product findById(Long id);
       
       /**
        * 查询产品列表
        */
       List<Product> findList(Product product);
       
       /**
        * 删除产品
        */
       boolean delete(Long id);
   }
   ```

3. **在基础设施模块（app-infrastructure）中添加PO类**

   ```java
   package cn.oalo.infrastructure.po;
   
   import com.baomidou.mybatisplus.annotation.IdType;
   import com.baomidou.mybatisplus.annotation.TableId;
   import com.baomidou.mybatisplus.annotation.TableName;
   import lombok.Data;
   import lombok.EqualsAndHashCode;
   
   @Data
   @EqualsAndHashCode(callSuper = true)
   @TableName("t_product")
   public class ProductPO extends BasePO {
       
       @TableId(value = "id", type = IdType.AUTO)
       private Long id;
       
       private String name;
       
       private String description;
       
       private BigDecimal price;
       
       private Integer stock;
   }
   ```

4. **在基础设施模块中添加Mapper接口**

   ```java
   package cn.oalo.infrastructure.mapper;
   
   import cn.oalo.infrastructure.po.ProductPO;
   import com.baomidou.mybatisplus.core.mapper.BaseMapper;
   import org.apache.ibatis.annotations.Mapper;
   
   @Mapper
   public interface ProductMapper extends BaseMapper<ProductPO> {
   }
   ```

5. **在基础设施模块中添加仓库实现类**

   ```java
   package cn.oalo.infrastructure.repository;
   
   import cn.oalo.domain.entity.Product;
   import cn.oalo.domain.repository.ProductRepository;
   import cn.oalo.infrastructure.converter.ProductConverter;
   import cn.oalo.infrastructure.mapper.ProductMapper;
   import cn.oalo.infrastructure.po.ProductPO;
   import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
   import lombok.RequiredArgsConstructor;
   import org.springframework.stereotype.Repository;
   
   import java.util.List;
   import java.util.stream.Collectors;
   
   @Repository
   @RequiredArgsConstructor
   public class ProductRepositoryImpl implements ProductRepository {
       
       private final ProductMapper productMapper;
       private final ProductConverter productConverter;
       
       @Override
       public Product save(Product product) {
           ProductPO productPO = productConverter.toPO(product);
           if (productPO.getId() == null) {
               productMapper.insert(productPO);
           } else {
               productMapper.updateById(productPO);
           }
           return productConverter.toEntity(productPO);
       }
       
       @Override
       public Product findById(Long id) {
           ProductPO productPO = productMapper.selectById(id);
           return productConverter.toEntity(productPO);
       }
       
       @Override
       public List<Product> findList(Product product) {
           LambdaQueryWrapper<ProductPO> queryWrapper = new LambdaQueryWrapper<>();
           // 添加查询条件
           if (product != null) {
               if (product.getName() != null) {
                   queryWrapper.like(ProductPO::getName, product.getName());
               }
           }
           List<ProductPO> list = productMapper.selectList(queryWrapper);
           return list.stream()
                   .map(productConverter::toEntity)
                   .collect(Collectors.toList());
       }
       
       @Override
       public boolean delete(Long id) {
           return productMapper.deleteById(id) > 0;
       }
   }
   ```

6. **在应用服务模块（app-application）中添加DTO类**

   ```java
   package cn.oalo.application.dto.request;
   
   import io.swagger.annotations.ApiModelProperty;
   import lombok.Data;
   
   import javax.validation.constraints.NotBlank;
   import javax.validation.constraints.NotNull;
   import java.math.BigDecimal;
   
   @Data
   public class ProductDTO {
       
       @ApiModelProperty(value = "产品ID")
       private Long id;
       
       @NotBlank(message = "产品名称不能为空")
       @ApiModelProperty(value = "产品名称", required = true)
       private String name;
       
       @ApiModelProperty(value = "产品描述")
       private String description;
       
       @NotNull(message = "产品价格不能为空")
       @ApiModelProperty(value = "产品价格", required = true)
       private BigDecimal price;
       
       @NotNull(message = "库存不能为空")
       @ApiModelProperty(value = "库存", required = true)
       private Integer stock;
   }
   ```

7. **在应用服务模块中添加转换器**

   ```java
   package cn.oalo.application.assembler;
   
   import cn.oalo.application.dto.request.ProductDTO;
   import cn.oalo.domain.entity.Product;
   import org.mapstruct.Mapper;
   import org.mapstruct.factory.Mappers;
   
   @Mapper(componentModel = "spring")
   public interface ProductDTOAssembler {
       
       ProductDTOAssembler INSTANCE = Mappers.getMapper(ProductDTOAssembler.class);
       
       /**
        * DTO转实体
        */
       Product toEntity(ProductDTO dto);
       
       /**
        * 实体转DTO
        */
       ProductDTO toDTO(Product entity);
   }
   ```

8. **在应用服务模块中添加服务接口和实现类**

   ```java
   package cn.oalo.application.service;
   
   import cn.oalo.application.dto.request.ProductDTO;
   import java.util.List;
   
   public interface ProductService {
       
       /**
        * 新增或修改产品
        */
       ProductDTO save(ProductDTO productDTO);
       
       /**
        * 查询产品详情
        */
       ProductDTO getById(Long id);
       
       /**
        * 查询产品列表
        */
       List<ProductDTO> getList(ProductDTO productDTO);
       
       /**
        * 删除产品
        */
       boolean delete(Long id);
   }
   ```

   ```java
   package cn.oalo.application.service.impl;
   
   import cn.oalo.application.assembler.ProductDTOAssembler;
   import cn.oalo.application.dto.request.ProductDTO;
   import cn.oalo.application.service.ProductService;
   import cn.oalo.domain.entity.Product;
   import cn.oalo.domain.repository.ProductRepository;
   import lombok.RequiredArgsConstructor;
   import org.springframework.stereotype.Service;
   import org.springframework.transaction.annotation.Transactional;
   
   import java.util.List;
   import java.util.stream.Collectors;
   
   @Service
   @RequiredArgsConstructor
   public class ProductServiceImpl implements ProductService {
       
       private final ProductRepository productRepository;
       private final ProductDTOAssembler productDTOAssembler;
       
       @Override
       @Transactional(rollbackFor = Exception.class)
       public ProductDTO save(ProductDTO productDTO) {
           Product product = productDTOAssembler.toEntity(productDTO);
           product = productRepository.save(product);
           return productDTOAssembler.toDTO(product);
       }
       
       @Override
       public ProductDTO getById(Long id) {
           Product product = productRepository.findById(id);
           return productDTOAssembler.toDTO(product);
       }
       
       @Override
       public List<ProductDTO> getList(ProductDTO productDTO) {
           Product product = productDTOAssembler.toEntity(productDTO);
           List<Product> list = productRepository.findList(product);
           return list.stream()
                   .map(productDTOAssembler::toDTO)
                   .collect(Collectors.toList());
       }
       
       @Override
       @Transactional(rollbackFor = Exception.class)
       public boolean delete(Long id) {
           return productRepository.delete(id);
       }
   }
   ```

9. **在接口模块（app-interfaces）中添加控制器**

   ```java
   package cn.oalo.interfaces.controller;
   
   import cn.oalo.application.dto.request.ProductDTO;
   import cn.oalo.application.service.ProductService;
   import cn.oalo.common.annotation.PreventDuplicateSubmit;
   import cn.oalo.common.api.R;
   import io.swagger.annotations.Api;
   import io.swagger.annotations.ApiOperation;
   import io.swagger.annotations.ApiParam;
   import lombok.RequiredArgsConstructor;
   import org.springframework.validation.annotation.Validated;
   import org.springframework.web.bind.annotation.*;
   
   import java.util.List;
   
   @Api(tags = "产品接口")
   @RestController
   @RequestMapping("/products")
   @RequiredArgsConstructor
   public class ProductController {
       
       private final ProductService productService;
       
       @PostMapping
       @ApiOperation(value = "新增或修改产品")
       @PreventDuplicateSubmit
       public R<ProductDTO> save(@Validated @RequestBody ProductDTO productDTO) {
           return R.ok(productService.save(productDTO));
       }
       
       @GetMapping("/{id}")
       @ApiOperation(value = "查询产品详情")
       public R<ProductDTO> getById(@ApiParam(value = "产品ID", required = true) @PathVariable Long id) {
           return R.ok(productService.getById(id));
       }
       
       @GetMapping("/list")
       @ApiOperation(value = "查询产品列表")
       public R<List<ProductDTO>> getList(ProductDTO productDTO) {
           return R.ok(productService.getList(productDTO));
       }
       
       @DeleteMapping("/{id}")
       @ApiOperation(value = "删除产品")
       public R<Boolean> delete(@ApiParam(value = "产品ID", required = true) @PathVariable Long id) {
           return R.ok(productService.delete(id));
       }
   }
   ```

10. **最后在基础设施模块中添加数据库脚本**

    创建 `app-infrastructure/src/main/resources/db/migration/V1_2__create_product_table.sql` 文件：

    ```sql
    CREATE TABLE IF NOT EXISTS t_product (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(100) NOT NULL COMMENT '产品名称',
        description TEXT COMMENT '产品描述',
        price DECIMAL(10, 2) NOT NULL COMMENT '产品价格',
        stock INT NOT NULL COMMENT '库存',
        create_time DATETIME COMMENT '创建时间',
        update_time DATETIME COMMENT '更新时间',
        create_by VARCHAR(50) COMMENT '创建人',
        update_by VARCHAR(50) COMMENT '更新人'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';
    ```

通过以上步骤，一个完整的业务模块就开发完成了，可以通过API接口进行测试。 