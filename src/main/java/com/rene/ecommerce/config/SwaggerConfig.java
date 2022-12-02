package com.rene.ecommerce.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Ecommerce API rest", version = "3.0", description = "Ecommerce API rest"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@Configuration
public class SwaggerConfig {
}
