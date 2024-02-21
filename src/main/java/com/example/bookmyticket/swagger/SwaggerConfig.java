package com.example.bookmyticket.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition
@EnableConfigurationProperties(value = SwaggerProperties.class)
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi(SwaggerProperties swaggerProperties) {
        return GroupedOpenApi.builder()
                .group(swaggerProperties.getGroup())
                .packagesToScan(swaggerProperties.getPackagesToScan())
                .displayName(swaggerProperties.getDisplayName())
                .build();
    }

    @Bean
    public OpenAPI openApi(SwaggerProperties swaggerProperties) {
        return new OpenAPI()
                .info(new Info().title(swaggerProperties.getApiTitle())
                        .description(swaggerProperties.getApiDescription())
                        .version(swaggerProperties.getApiVersion())
                        .license(new License().name(swaggerProperties.getLicenseName())
                                .url(swaggerProperties.getLicenseUrl()))
                        .contact(new Contact().name(swaggerProperties.getContactTeam())
                                .url(swaggerProperties.getContactConfluence())
                                .email(swaggerProperties.getContactEmail())))
                .externalDocs(new ExternalDocumentation()
                        .description(swaggerProperties.getExternalDocDescription())
                        .url(swaggerProperties.getExternalDocUrl()));


    }
}
