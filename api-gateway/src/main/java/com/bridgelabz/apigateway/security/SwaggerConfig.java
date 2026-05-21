package com.bridgelabz.apigateway.security;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public List<GroupedOpenApi> apis() {

        return List.of(

                GroupedOpenApi.builder()
                        .group("auth-service")
                        .pathsToMatch("/auth/**")
                        .addOpenApiCustomizer(openApi ->
                                openApi.info(new io.swagger.v3.oas.models.info.Info()
                                        .title("Auth Service API")))
                        .build(),

                GroupedOpenApi.builder()
                        .group("quantity-service")
                        .pathsToMatch("/quantity/**")
                        .addOpenApiCustomizer(openApi ->
                                openApi.info(new io.swagger.v3.oas.models.info.Info()
                                        .title("Quantity Service API")))
                        .build()
        );
    }
}