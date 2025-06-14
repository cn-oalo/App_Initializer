# FastJSON迁移到Jackson说明

## 1. 迁移背景

为了提高项目的安全性和稳定性，我们决定将项目中使用的JSON序列化工具从FastJSON迁移到Jackson。主要原因如下：

1. Jackson是Spring Boot官方推荐的JSON处理库，与Spring生态系统更加兼容
2. Jackson在处理复杂对象和日期时间类型时更加稳定
3. Jackson有更好的安全性记录，FastJSON历史上曾出现过多个安全漏洞
4. Jackson有更好的社区支持和更频繁的更新维护

## 2. 主要变更

### 2.1 依赖变更

- 移除了FastJSON相关依赖：
  ```xml
  <dependency>
      <groupId>com.alibaba.fastjson2</groupId>
      <artifactId>fastjson2</artifactId>
  </dependency>
  ```

- 添加了Jackson相关依赖：
  ```xml
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
  </dependency>
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
  </dependency>
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
  </dependency>
  <dependency>
      <groupId>com.fasterxml.jackson.datatype</groupId>
      <artifactId>jackson-datatype-jsr310</artifactId>
  </dependency>
  ```

### 2.2 类变更

- 删除了 `FastJson2JsonRedisSerializer` 类
- 新增了 `JacksonRedisSerializer` 类用于Redis序列化
- 新增了 `JsonUtils` 工具类，提供与FastJSON兼容的API

### 2.3 配置变更

- 修改了 `RedisConfig` 类，使用 `JacksonRedisSerializer` 替代 `FastJson2JsonRedisSerializer`

## 3. API对照表

为了方便开发人员迁移代码，以下是FastJSON和Jackson API的对照表：

| FastJSON | Jackson (JsonUtils) | 说明 |
|---------|-------------------|------|
| `JSON.toJSONString(obj)` | `JsonUtils.toJsonString(obj)` | 对象转JSON字符串 |
| `JSON.parseObject(str, clazz)` | `JsonUtils.parseObject(str, clazz)` | JSON字符串转对象 |
| `JSON.parseArray(str, clazz)` | `JsonUtils.parseArray(str, clazz)` | JSON字符串转列表 |
| `JSON.parseObject(str)` | `JsonUtils.parseMap(str)` | JSON字符串转Map |
| `JSON.parseObject(str, new TypeReference<T>(){})` | `JsonUtils.parseObject(str, new TypeReference<T>(){})` | JSON字符串转复杂类型 |

## 4. 注解对照表

| FastJSON | Jackson | 说明 |
|---------|--------|------|
| `@JSONField(name = "field_name")` | `@JsonProperty("field_name")` | 指定字段名 |
| `@JSONField(serialize = false)` | `@JsonIgnore` | 忽略字段 |
| `@JSONField(format = "yyyy-MM-dd")` | `@JsonFormat(pattern = "yyyy-MM-dd")` | 日期格式化 |
| `@JSONField(ordinal = 1)` | `@JsonPropertyOrder` | 字段排序 |
| `@JSONField(deserialize = false)` | `@JsonIgnore` | 反序列化忽略 |

## 5. 迁移步骤

1. 更新项目依赖，添加Jackson依赖，移除FastJSON依赖
2. 使用 `JsonUtils` 工具类替代直接使用 `JSON` 类
3. 将FastJSON注解替换为对应的Jackson注解
4. 使用 `JacksonRedisSerializer` 替代 `FastJson2JsonRedisSerializer`
5. 对于复杂的序列化/反序列化场景，参考Jackson文档进行调整

## 6. 注意事项

1. Jackson和FastJSON在处理日期时间类型时有差异，需要特别注意
2. Jackson默认不忽略空值，需要通过配置 `objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)` 实现
3. Jackson在处理循环引用时可能会抛出异常，需要使用 `@JsonManagedReference` 和 `@JsonBackReference` 注解处理
4. 对于使用FastJSON特有功能的代码，需要进行特殊处理和测试

## 7. 参考资料

- [Jackson官方文档](https://github.com/FasterXML/jackson)
- [Jackson注解指南](https://www.baeldung.com/jackson-annotations)
- [Spring Boot中的JSON处理](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.json) 