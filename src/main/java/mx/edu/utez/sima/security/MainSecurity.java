package mx.edu.utez.sima.security;

import mx.edu.utez.sima.security.filters.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@Configuration
@EnableWebSecurity
public class MainSecurity {

    // Roles del sistema:
    // ADMIN - Administrador del sistema (puede hacer todo)
    // RESPONSIBLE - Responsable de almacén (permisos limitados)

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
        http.csrf(c -> c.disable())
                .cors(c -> c.configurationSource(corsRegistry()))
                .authorizeHttpRequests(auth -> auth

                        // ========== ENDPOINTS PÚBLICOS ==========
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()

                        // ========== GESTIÓN DE USUARIOS ==========
                        // Solo ADMIN puede gestionar usuarios (crear, editar, eliminar, listar)
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        // ========== GESTIÓN DE ROLES ==========
                        // Solo ADMIN puede gestionar roles
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")

                        // ========== GESTIÓN DE CATEGORÍAS ==========
                        // Solo ADMIN puede crear, editar y eliminar categorías
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                        // Ambos roles pueden consultar categorías
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyRole("ADMIN", "RESPONSIBLE")

                        // ========== GESTIÓN DE ALMACENES ==========
                        // Solo ADMIN puede crear, editar y eliminar almacenes
                        .requestMatchers(HttpMethod.POST, "/api/storages/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/storages/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/storages/**").hasRole("ADMIN")
                        // ADMIN puede ver todos los almacenes
                        .requestMatchers(HttpMethod.GET, "/api/storages").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/storages/all").hasRole("ADMIN")
                        // RESPONSIBLE puede ver información de almacenes (especialmente el suyo)
                        .requestMatchers(HttpMethod.GET, "/api/storages/**").hasAnyRole("ADMIN", "RESPONSIBLE")

                        // ========== GESTIÓN DE ARTÍCULOS ==========
                        // Solo ADMIN puede crear, editar y eliminar artículos
                        .requestMatchers(HttpMethod.POST, "/api/articles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/articles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/articles/**").hasRole("ADMIN")
                        // Ambos roles pueden consultar artículos
                        .requestMatchers(HttpMethod.GET, "/api/articles/**").hasAnyRole("ADMIN", "RESPONSIBLE")

                        // ========== GESTIÓN DE INVENTARIO ==========
                        // ADMIN puede hacer todo con el inventario
                        .requestMatchers(HttpMethod.GET, "/api/inventory/**").hasAnyRole("ADMIN", "RESPONSIBLE")
                        .requestMatchers(HttpMethod.POST, "/api/inventory/**").hasAnyRole("ADMIN", "RESPONSIBLE")
                        .requestMatchers(HttpMethod.PUT, "/api/inventory/**").hasAnyRole("ADMIN", "RESPONSIBLE")
                        // Solo ADMIN puede eliminar registros de inventario
                        .requestMatchers(HttpMethod.DELETE, "/api/inventory/**").hasRole("ADMIN")

                        // ========== REPORTES Y CONSULTAS ==========
                        // Reportes generales - Solo ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/reports/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/**").hasRole("ADMIN")

                        // Consultas específicas de almacén - Ambos roles
                        .requestMatchers(HttpMethod.GET, "/api/storages/*/inventory").hasAnyRole("ADMIN", "RESPONSIBLE")
                        .requestMatchers(HttpMethod.GET, "/api/storages/*/articles").hasAnyRole("ADMIN", "RESPONSIBLE")

                        // ========== PERFIL DE USUARIO ==========
                        // Cualquier usuario autenticado puede ver/actualizar su propio perfil
                        .requestMatchers(HttpMethod.GET, "/api/profile").hasAnyRole("ADMIN", "RESPONSIBLE")
                        .requestMatchers(HttpMethod.PUT, "/api/profile").hasAnyRole("ADMIN", "RESPONSIBLE")

                        // ========== ENDPOINTS ESPECÍFICOS DEL SISTEMA ==========

                        // Asignación de responsables a almacenes - Solo ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/storages/*/assign-responsible").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/storages/*/remove-responsible").hasRole("ADMIN")

                        // Agregar/quitar artículos de almacenes - Ambos roles
                        .requestMatchers(HttpMethod.POST, "/api/storages/*/articles").hasAnyRole("ADMIN", "RESPONSIBLE")
                        .requestMatchers(HttpMethod.PUT, "/api/storages/*/articles/*/quantity").hasAnyRole("ADMIN", "RESPONSIBLE")
                        .requestMatchers(HttpMethod.DELETE, "/api/storages/*/articles/*").hasAnyRole("ADMIN", "RESPONSIBLE")

                        // Consultas por categoría - Ambos roles
                        .requestMatchers(HttpMethod.GET, "/api/categories/*/storages").hasAnyRole("ADMIN", "RESPONSIBLE")
                        .requestMatchers(HttpMethod.GET, "/api/categories/*/articles").hasAnyRole("ADMIN", "RESPONSIBLE")

                        // Búsquedas y filtros - Ambos roles
                        .requestMatchers(HttpMethod.GET, "/api/search/**").hasAnyRole("ADMIN", "RESPONSIBLE")

                        // ========== CUALQUIER OTRA PETICIÓN ==========
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Agregar el filtro JWT antes del filtro de autenticación por usuario/contraseña
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración CORS para permitir peticiones desde el frontend
     */
    private CorsConfigurationSource corsRegistry() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos (cambiar en producción por dominio específico)
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200", "*"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers permitidos
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir credenciales (cambiar según necesidades)
        configuration.setAllowCredentials(false);

        // Aplicar configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Bean para encriptar contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

/*
 * RESUMEN DE PERMISOS POR ROL:
 *
 * ADMIN (Administrador):
 * - Gestión completa de usuarios, roles, categorías, almacenes y artículos
 * - Asignación de responsables a almacenes
 * - Acceso a todos los reportes y estadísticas
 * - Eliminación de registros de inventario
 * - Acceso a dashboard administrativo
 *
 * RESPONSIBLE (Responsable de Almacén):
 * - Consulta de categorías, almacenes y artículos
 * - Gestión de inventario (agregar/actualizar cantidades)
 * - Consulta del inventario de su almacén asignado
 * - Actualización de su perfil
 * - Búsquedas y filtros básicos
 *
 * VALIDACIONES ADICIONALES A IMPLEMENTAR EN SERVICIOS:
 * - El RESPONSIBLE solo debe poder gestionar el inventario de SU almacén asignado
 * - Validar que los artículos agregados sean de la misma categoría que el almacén
 * - Un almacén solo puede tener un responsable asignado
 */