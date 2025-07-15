package mx.edu.utez.sima.security.filters;

// Todos lo filtros se crean con la notation @Component

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import mx.edu.utez.sima.security.jwt.JWTUtils;
import mx.edu.utez.sima.security.jwt.UDService;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private UDService udService; // Sirve para validar el token

    @Autowired
    private JWTUtils jwtUtils; // Sirve para manipular el token

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String AUTHORIZATION_HEADER = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if (AUTHORIZATION_HEADER != null && AUTHORIZATION_HEADER.startsWith("Bearer ")){
            token = AUTHORIZATION_HEADER.substring(7);
            username = jwtUtils.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = udService.loadUserByUsername(username);

            if (jwtUtils.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
