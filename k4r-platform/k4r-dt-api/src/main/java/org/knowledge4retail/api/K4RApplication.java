package org.knowledge4retail.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
        info = @Info(title = "K4R DT API", version = "1", description = "K4R DT API reference")
)
@SpringBootApplication
@EnableSpringDataWebSupport
@EnableScheduling
@EnableCaching
public class K4RApplication {
    public static void main(String[] args) {
        SpringApplication.run(K4RApplication.class, args);
    }
}
