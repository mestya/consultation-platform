package com.tju.consultationPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
/***
 * 基于注解的auditing元数据
 *
 * @CreatedBy,
 * @LastModifiedBy ：捕获实体是被谁创建的或者修改
 * @CreatedDate ：捕获创建的时间
 * @LastModifiedDate ：捕获修改的时间
 */
public class FortheagedApplication {

    public static void main(String[] args) {

        SpringApplication.run(FortheagedApplication.class, args);
    }
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
