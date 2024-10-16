package com.sprve.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private Info info(){
        return new Info()
                .title("Sprve API")
                .description("A test project for Sprve.")
                .version("v1.0.0")
                .license(license());
    }

    private License license() {
        return new License()
                .name("Sprve")
                .url("https://opensource.org/licenses/Sprve");
    }

    private ExternalDocumentation externalDocumentation() {
        return new ExternalDocumentation()
                .description("这是一个额外的描述。")
                .url("https://sprve.github.io/");
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(info())
                .externalDocs(externalDocumentation());
    }
}
