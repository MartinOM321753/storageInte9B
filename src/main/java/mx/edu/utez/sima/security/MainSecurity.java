package mx.edu.utez.sima.security;

import mx.edu.utez.sima.security.filters.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class MainSecurity {

    @Autowired
    private JWTFilter jwtFilter;

    // Endpoints públicos para Swagger
    private final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-resources/**",
            "/webjars/**"
    };

    // Endpoints públicos (sin autenticación)
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**"  // Login y endpoints de autenticación
    };

    @Bean
    public SecurityFilterChain doFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsRegistry()))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()

                        // === Usuarios ===
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        // === Categorías de almacén ===
                        .requestMatchers(HttpMethod.GET, "/api/category/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/**").hasRole("ADMIN")

                        // === Almacenes ===
                        .requestMatchers(HttpMethod.GET, "/api/storage/responsible/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/storage").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/storage/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/storage/**").hasRole("ADMIN")

                        // === Artículos ===
                        .requestMatchers(HttpMethod.GET, "/api/article/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/article").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/article/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/article/**").hasRole("USER")

                        // === Asignación y remoción de artículos en almacén ===
                        .requestMatchers(HttpMethod.PUT, "/api/storage/{storageId}/article/{articleId}").hasRole("USER") // Asignar
                        .requestMatchers(HttpMethod.DELETE, "/api/storage/{storageId}/article/{articleId}").hasRole("USER") // Remover


                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()

                        // ========== CUALQUIER OTRA PETICIÓN ==========
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // Agregar el filtro JWT antes del filtro de autenticación por usuario/contraseña
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración CORS para permitir peticiones desde el frontend
     */

    private CorsConfigurationSource corsRegistry() {
        // ¿Qué queremos configurar?
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false); // Esto es para cookies

        // ¿En dónde lo queremos aplicar?
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
