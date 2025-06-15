package cn.oalo.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动程序
 */
@SpringBootApplication
@ComponentScan(basePackages = {"cn.oalo"})
@MapperScan({"cn.oalo.**.mapper"})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  企业级应用框架启动成功   ლ(´ڡ`ლ)ﾞ");
    }
} 