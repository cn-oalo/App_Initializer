# 企业级应用框架

基于Spring Boot 2.7.x、MyBatis-Plus等技术栈的企业级应用框架，采用DDD（领域驱动设计）分层架构。

## 项目结构

```
enterprise-app
├── app-common             # 公共模块
├── app-domain             # 领域模型模块
├── app-infrastructure     # 基础设施模块
├── app-application        # 应用服务模块
├── app-interfaces         # 接口模块
└── app-boot               # 启动模块
```

## 技术栈

- Spring Boot 2.7.14
- Spring Security + JWT
- MyBatis-Plus 3.5.3
- MySQL 8.0
- Druid 连接池
- Redis
- Swagger 3.0 / Knife4j
- HuTool、MapStruct等工具库

## 功能特性

- 基于DDD的分层架构
- 统一响应格式和全局异常处理
- JWT认证和权限控制
- 用户、角色管理
- API文档（Swagger/Knife4j）
- 数据库连接池监控（Druid）

## 安装使用

### 环境要求

- JDK 1.8+
- Maven 3.5+
- MySQL 8.0+
- Redis 5.0+

### 数据库初始化

1. 执行 `app-boot/src/main/resources/db/schema.sql` 初始化数据库

### 项目启动

1. 克隆项目
2. 修改 `app-boot/src/main/resources/application.yml` 中的数据库和Redis配置
3. 执行 `mvn clean package` 打包
4. 执行 `java -jar app-boot/target/app-boot-0.0.1-SNAPSHOT.jar` 启动项目
5. 访问 `http://localhost:8080/api/swagger-ui/index.html` 查看API文档

### 默认账号

- 管理员：admin / admin123

## 接口说明

### 认证接口

- 登录：POST /api/auth/login
- 注册：POST /api/auth/register

### 用户接口

- 获取当前用户信息：GET /api/users/current
- 获取用户信息：GET /api/users/{id}
- 分页查询用户列表：GET /api/users/page
- 更新用户状态：PUT /api/users/{id}/status
- 删除用户：DELETE /api/users/{id}
- 分配用户角色：POST /api/users/{id}/roles

## 联系方式

- 联系人：OALO
- 邮箱：contact@oalo.cn
- 网站：https://oalo.cn 