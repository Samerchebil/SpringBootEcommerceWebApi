package com.rene.ecommerce.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String apiTitle = "Ecommerce REST API";
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components().addSecuritySchemes(securitySchemeName,
                                new io.swagger.v3.oas.models.security.SecurityScheme().name(securitySchemeName).type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title(apiTitle).version("v1.0.0"));
    }
}
