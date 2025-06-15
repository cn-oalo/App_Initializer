package cn.oalo.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j配置
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("企业级应用框架API文档")
                        .description("企业级应用框架API文档")
                        .version("1.0.0")
                        .build())
                .groupName("管理端接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.oalo.admin.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket systemApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("企业级应用框架API文档")
                        .description("企业级应用框架API文档")
                        .version("1.0.0")
                        .build())
                .groupName("系统模块接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.oalo.web.controller.system"))
                .paths(PathSelectors.any())
                .build();
    }
} 