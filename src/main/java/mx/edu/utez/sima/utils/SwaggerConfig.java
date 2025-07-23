package mx.edu.utez.sima.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI config() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST de almacenes")
                        .description("Documentación de los endpoints del servicio de almacenes")
                        .version("V1.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")) // <<-- Aplica seguridad
                .components(new Components().addSecuritySchemes("bearerAuth", // <<-- Define esquema
                        new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)));
    }
}
