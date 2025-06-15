package cn.oalo.admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j配置
 */
@Configuration
public class Knife4jConfig {

    @Value("${swagger.enabled:true}")
    private boolean enabled;
    
    @Value("${swagger.title:API文档}")
    private String title;
    
    @Value("${swagger.description:API文档}")
    private String description;
    
    @Value("${swagger.version:1.0.0}")
    private String version;
    
    @Value("${swagger.contact.name:}")
    private String contactName;
    
    @Value("${swagger.contact.url:}")
    private String contactUrl;
    
    @Value("${swagger.contact.email:}")
    private String contactEmail;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact()
                                .name(contactName)
                                .url(contactUrl)
                                .email(contactEmail)));
    }
} 