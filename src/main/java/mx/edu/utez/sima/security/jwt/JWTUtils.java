package mx.edu.utez.sima.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Cuarto paso: Generar las utilerias para jwt
@Service
public class JWTUtils {
    @Value("${secret.key}")
    private String SECRET_KEY;

    // Esta función ayuda a extraer todas las propiedades del token
    // Es decir, el cuerpo del token
    public Claims exctractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Esta función nos ayuda a poder extraer las propiedades del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims CLAIMS = exctractAllClaims(token);
        return claimsResolver.apply(CLAIMS);
    }

    // Esta funcion extrae el nombre de usuario del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Esta funcion extrae la fecha de expiracion del token
    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Esta funcion valida si el token esta expirado
    public Boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    // Esta funcion consume la funcion de arriba, tambien verifica que coincida el nombre de usuario
    public Boolean validateToken (String token, UserDetails userDetails) {
        final String USERNAME = extractUsername(token);
        return (USERNAME.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Esta funcion nos ayuda a crear nuestro token
    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder() // Aquí decimos que vamos a construir un token
                .setClaims(claims).setSubject(subject) // Aquí mandamos la información del usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // Aquí le decimos cuando se creó el token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Cuanto va a durar
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Aquí lo firmamos
                .compact(); // Terminamos por compactar el token
    }

    // Esta funcion consume la funcion de crear solo para retornar
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
}