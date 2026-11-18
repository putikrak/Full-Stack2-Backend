package cl.esports.arena_manager_backend.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("eSports Arena Manager API")
                        .version("1.0")
                        .description("API REST para la gestión de torneos, equipos, jugadores, inscripciones y sanciones con autenticación JWT. "
                                + "Para probar endpoints protegidos, inicia sesión en POST /api/v1/auth/login, copia el token y pégalo en el botón 'Authorize'."));
    }
}