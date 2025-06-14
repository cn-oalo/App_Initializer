# 企业级应用框架

一个基于 Spring Boot 的企业级应用框架，采用分层架构。

## 技术选型

- JDK 1.8+
- Spring Boot 2.7.14
- Spring Security
- MyBatis-Plus 3.5.3
- MySQL 5.7+
- Redis
- alibaba Druid
- aliyun OSS
- JWT
- Swagger
- slf4j

## 项目结构

```
cn-oalo
├── cn-oalo-admin        // 后台管理模块
├── cn-oalo-common       // 公共工具模块
├── cn-oalo-framework    // 框架核心模块
├── cn-oalo-generator    // 代码生成模块
└── cn-oalo-system       // 系统功能模块
```

## 功能特性

- 统一响应格式和全局异常处理
- 基于JWT的认证和授权
- 多数据源支持
- 防止重复提交
- 自动填充创建时间、修改时间、创建人、修改人
- OSS文件上传下载
- 国际化支持
- ThreadLocal用户上下文
- jackson序列化工具
- 完善的XSS防范及脚本过滤，杜绝XSS攻击
- Maven多项目依赖，模块及插件分项目，尽量松耦合，方便模块升级、增减模块。

## 快速开始

### 前置条件

- JDK 1.8 或更高版本
- Maven 3.6 或更高版本
- MySQL 5.7 或更高版本
- Redis 5.0 或更高版本

### 运行步骤

1. 克隆项目到本地
2. 创建数据库 `cn_oalo`
3. 执行 `cn-oalo-admin/src/main/resources/db/schema.sql` 初始化表结构和数据
4. 修改 `cn-oalo-admin/src/main/resources/application.yml` 中的数据库连接信息和Redis连接信息
5. 在项目根目录执行 `mvn clean install` 编译项目
6. 运行 `cn-oalo-admin` 模块的 `AdminApplication.java` 启动项目
7. 访问 `http://localhost:8080/api/doc.html` 查看接口文档

### 默认用户

- 管理员账号：admin，密码：123456
- 测试账号：test，密码：123456

## 开发指南

### 模块说明

- **cn-oalo-common**：公共工具模块，包含工具类、异常、常量等
- **cn-oalo-framework**：框架核心模块，包含安全、数据库、缓存、文件上传等配置
- **cn-oalo-system**：系统功能模块，包含用户、角色、菜单等系统核心功能
- **cn-oalo-generator**：代码生成模块，用于生成基础代码
- **cn-oalo-admin**：后台管理模块，包含启动类和配置文件

### 开发流程

1. 在 `cn-oalo-system` 模块中创建实体类
2. 在 `cn-oalo-system` 模块中创建Mapper接口和XML文件
3. 在 `cn-oalo-system` 模块中创建Service接口和实现类
4. 在 `cn-oalo-admin` 模块中创建Controller类

## 贡献指南

1. Fork 本仓库
2. 创建新的分支 `git checkout -b feature/your-feature`
3. 提交你的修改 `git commit -m 'Add some feature'`
4. 推送到分支 `git push origin feature/your-feature`
5. 提交 Pull Request

## 许可证

[MIT](LICENSE) 