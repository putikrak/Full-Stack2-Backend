package cl.esports.arena_manager_backend.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Value("${CORS_ORIGENES:http://localhost:5173,http://127.0.0.1:5173,https://fullstack2-main-1.vercel.app}")
    private String corsOrigenes;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            boolean vencido = "vencido".equals(request.getAttribute(JwtFilter.ATRIBUTO_ERROR));
                            escribirJsonError(response, 401, vencido
                                    ? "Tu sesión expiró. Vuelve a iniciar sesión."
                                    : "Debes iniciar sesión para realizar esta acción.");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                escribirJsonError(response, 403, "Tu rol no tiene permiso para realizar esta acción.")
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        // Preflight CORS y documentación Swagger
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll()

                        // Rutas públicas (autenticación y consultas generales)
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/juegos", "/api/v1/juegos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/torneos", "/api/v1/torneos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/equipos", "/api/v1/equipos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/jugadores", "/api/v1/jugadores/**").permitAll()

                        // Rutas protegidas por rol
                        .requestMatchers(HttpMethod.POST, "/api/v1/juegos").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/torneos").hasAnyRole("ADMINISTRADOR", "ORGANIZADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/torneos/**").hasAnyRole("ADMINISTRADOR", "ORGANIZADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/torneos/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/api/v1/equipos", "/api/v1/equipos/**").hasAnyRole("JUGADOR", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/equipos/**").hasAnyRole("JUGADOR", "ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/inscripciones").hasAnyRole("ADMINISTRADOR", "ORGANIZADOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/inscripciones").hasAnyRole("JUGADOR", "ADMINISTRADOR")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/jugadores/**").hasAnyRole("JUGADOR", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/sanciones").hasAnyRole("ADMINISTRADOR", "ORGANIZADOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origenes = Arrays.stream(corsOrigenes.split(","))
                .map(String::trim)
                .toList();
        config.setAllowedOrigins(origenes);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void escribirJsonError(HttpServletResponse response, int status, String mensaje) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"mensaje\":\"" + mensaje.replace("\"", "\\\"") + "\"}");
    }
}