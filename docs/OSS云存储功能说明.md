# OSS云存储功能说明

## 1. 功能概述

OSS云存储功能是对文件上传下载模块的扩展，支持将文件存储到各种云存储服务中，目前已实现的存储类型包括：

- 本地存储（LOCAL）
- 阿里云OSS（ALIYUN_OSS）
- 腾讯云COS（预留）
- 七牛云Kodo（预留）
- MinIO（预留）

通过策略模式和工厂模式的设计，可以轻松扩展更多的存储类型，而不需要修改现有代码。

## 2. 技术实现

### 2.1 核心组件

- **FileStorage**: 文件存储策略接口，定义了文件上传、下载、删除等操作
- **LocalFileStorage**: 本地文件存储实现
- **AliyunOssStorage**: 阿里云OSS存储实现
- **FileStorageFactory**: 文件存储工厂，根据配置选择合适的存储策略
- **OssConfig**: OSS配置类，提供OSS客户端和属性配置

### 2.2 数据库设计

在`sys_file`表中添加了`storage_type`字段，用于标识文件的存储类型：

- 0: 本地存储（LOCAL）
- 1: 阿里云OSS（ALIYUN_OSS）
- 2: 腾讯云COS（TENCENT_COS）
- 3: 七牛云Kodo（QINIU_KODO）
- 4: MinIO（MINIO）

### 2.3 配置设计

在`application.yml`中添加了文件存储类型配置和各种云存储服务的配置：

```yaml
# 文件存储类型
file:
  storage:
    type: LOCAL  # 可选值：LOCAL, ALIYUN_OSS, TENCENT_COS, QINIU_KODO, MINIO

# 阿里云OSS配置
oss:
  endpoint: oss-cn-beijing.aliyuncs.com
  accessKeyId: your-access-key-id
  accessKeySecret: your-access-key-secret
  bucketName: your-bucket-name
  domain: https://your-bucket-name.oss-cn-beijing.aliyuncs.com
```

## 3. 使用方法

### 3.1 配置存储类型

在`application.yml`中配置`file.storage.type`属性，可选值包括：

- LOCAL: 本地存储
- ALIYUN_OSS: 阿里云OSS
- TENCENT_COS: 腾讯云COS
- QINIU_KODO: 七牛云Kodo
- MINIO: MinIO

### 3.2 配置云存储参数

以阿里云OSS为例，需要在`application.yml`中配置以下参数：

```yaml
oss:
  endpoint: oss-cn-beijing.aliyuncs.com  # OSS端点
  accessKeyId: your-access-key-id  # 访问密钥ID
  accessKeySecret: your-access-key-secret  # 访问密钥密钥
  bucketName: your-bucket-name  # 存储桶名称
  domain: https://your-bucket-name.oss-cn-beijing.aliyuncs.com  # 自定义域名（可选）
```

### 3.3 使用示例

#### 上传文件

文件上传时会自动根据配置的存储类型选择对应的存储策略：

```java
@Autowired
private SysFileService fileService;

// 上传文件
public void uploadFile(MultipartFile file) {
    try {
        SysFileVO fileInfo = fileService.upload(file);
        System.out.println("上传成功，文件ID：" + fileInfo.getFileId());
        System.out.println("存储类型：" + fileInfo.getStorageTypeName());
        System.out.println("文件URL：" + fileInfo.getUrl());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

#### 下载文件

```java
@Autowired
private SysFileService fileService;

// 下载文件
public void downloadFile(Long fileId, HttpServletResponse response) {
    try {
        fileService.download(fileId, response);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

#### 根据路径下载文件

```java
@Autowired
private FileStorageFactory fileStorageFactory;

// 根据路径下载文件
public void downloadFileByPath(String filePath, Integer storageType, HttpServletResponse response) {
    try {
        StorageType type = StorageType.values()[storageType];
        FileStorage storage = fileStorageFactory.getFileStorage(type);
        
        // 获取文件流
        InputStream inputStream = storage.getFileStream(filePath);
        
        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        
        // 写入响应
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
        inputStream.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

## 4. 扩展其他云存储

如果需要扩展其他云存储服务，只需要实现`FileStorage`接口并注册到Spring容器中即可。以腾讯云COS为例：

### 4.1 添加依赖

```xml
<!-- 腾讯云COS依赖 -->
<dependency>
    <groupId>com.qcloud</groupId>
    <artifactId>cos_api</artifactId>
    <version>5.6.89</version>
</dependency>
```

### 4.2 添加配置类

```java
@Configuration
public class CosConfig {

    @Bean
    public COSClient cosClient(CosProperties cosProperties) {
        // 初始化客户端配置
        COSCredentials cred = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(cosProperties.getRegion()));
        return new COSClient(cred, clientConfig);
    }

    @Bean
    @ConfigurationProperties(prefix = "cos")
    public CosProperties cosProperties() {
        return new CosProperties();
    }

    public static class CosProperties {
        private String secretId;
        private String secretKey;
        private String region;
        private String bucketName;
        private String domain;
        
        // getter and setter
    }
}
```

### 4.3 实现存储策略

```java
@Component
public class TencentCosStorage implements FileStorage {

    @Autowired
    private COSClient cosClient;

    @Autowired
    private CosConfig.CosProperties cosProperties;

    @Override
    public String upload(MultipartFile file, String fileName) throws IOException {
        return upload(file.getInputStream(), fileName);
    }

    @Override
    public String upload(InputStream inputStream, String fileName) throws IOException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            PutObjectRequest request = new PutObjectRequest(cosProperties.getBucketName(), fileName, inputStream, metadata);
            cosClient.putObject(request);
            return fileName;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            cosClient.deleteObject(cosProperties.getBucketName(), filePath);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        if (cosProperties.getDomain() != null && !cosProperties.getDomain().isEmpty()) {
            return cosProperties.getDomain() + "/" + filePath;
        }
        return "https://" + cosProperties.getBucketName() + ".cos." + cosProperties.getRegion() + ".myqcloud.com/" + filePath;
    }

    @Override
    public InputStream getFileStream(String filePath) throws IOException {
        COSObject cosObject = cosClient.getObject(cosProperties.getBucketName(), filePath);
        return cosObject.getObjectContent();
    }

    @Override
    public StorageType getType() {
        return StorageType.TENCENT_COS;
    }
}
```

### 4.4 添加配置

```yaml
# 腾讯云COS配置
cos:
  secretId: your-secret-id
  secretKey: your-secret-key
  region: ap-beijing
  bucketName: your-bucket-name
  domain: https://your-bucket-name.cos.ap-beijing.myqcloud.com
```

## 5. 注意事项

1. 不同云存储服务的API和参数可能不同，需要根据各自的文档进行适配
2. 云存储服务通常按流量和存储空间计费，需要注意控制成本
3. 对于大文件上传，建议使用分片上传功能，本模块暂未实现
4. 文件URL的生成方式可能因云存储服务而异，需要根据实际情况调整
5. 建议对敏感文件进行加密处理，并设置适当的访问权限
6. 定期清理无用的文件，避免占用过多存储空间 

classDiagram
    class FileStorage {
        <<interface>>
        +upload(MultipartFile file, String fileName) String
        +upload(InputStream inputStream, String fileName) String
        +delete(String filePath) boolean
        +getFileUrl(String filePath) String
        +getFileStream(String filePath) InputStream
        +getType() StorageType
    }
    
    class LocalFileStorage {
        -FileUploadProperties fileUploadProperties
        -String uploadPath
        +upload(MultipartFile file, String fileName) String
        +upload(InputStream inputStream, String fileName) String
        +delete(String filePath) boolean
        +getFileUrl(String filePath) String
        +getFileStream(String filePath) InputStream
        +getType() StorageType
    }
    
    class AliyunOssStorage {
        -OSS ossClient
        -OssProperties ossProperties
        +upload(MultipartFile file, String fileName) String
        +upload(InputStream inputStream, String fileName) String
        +delete(String filePath) boolean
        +getFileUrl(String filePath) String
        +getFileStream(String filePath) InputStream
        +getType() StorageType
    }
    
    class FileStorageFactory {
        -String storageType
        -Map~StorageType, FileStorage~ storageMap
        +getFileStorage() FileStorage
        +getFileStorage(StorageType type) FileStorage
    }
    
    class StorageType {
        <<enumeration>>
        LOCAL
        ALIYUN_OSS
        TENCENT_COS
        QINIU_KODO
        MINIO
    }
    
    class SysFileServiceImpl {
        -SysFileMapper fileMapper
        -FileUploadProperties fileUploadProperties
        -RedisCache redisCache
        -FileStorageFactory fileStorageFactory
        -String storageType
        +upload(MultipartFile file) SysFileVO
        +download(Long fileId, HttpServletResponse response) void
        +deleteFile(Long fileId) boolean
        +getFileInfo(Long fileId) SysFileVO
        +listFilesByName(String fileName) List~SysFileVO~
    }
    
    FileStorage <|.. LocalFileStorage : implements
    FileStorage <|.. AliyunOssStorage : implements
    FileStorageFactory --> FileStorage : creates
    FileStorageFactory --> StorageType : uses
    SysFileServiceImpl --> FileStorageFactory : uses
    SysFileServiceImpl --> StorageType : uses