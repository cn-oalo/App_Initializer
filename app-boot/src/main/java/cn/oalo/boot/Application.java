package cn.oalo.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 应用启动类
 */
@SpringBootApplication
@ComponentScan("cn.oalo")
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
} 