package com.opositaweb.util;

import com.opositaweb.config.properties.OpositaWebProperties;
import com.opositaweb.repository.entities.Customer;
import com.opositaweb.repository.jpa.TokenJpaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final OpositaWebProperties opositaWebProperties;

    private final TokenJpaRepository tokenJpaRepository;

    // Extrae el nombre de usuario (subject) del token JWT.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Verifica si un token es válido comparando el usuario, la expiración y si está activo en la base de datos.
    public boolean isValid(String token, UserDetails user){
        return isTokenUsernameMatchingUser(token, user.getUsername())
                && !isTokenExpired(token)
                && isTokenActive(token);
    }

    // Verifica si un refresh token es válido comparando el usuario, la expiración y si está activo en la base de datos.
    public boolean isValidRefreshToken(String token, Customer customer){
        return isTokenUsernameMatchingUser(token, customer.getUsername())
                && !isTokenExpired(token)
                && isRefreshTokenActive(token);
    }

    // Compara el usuario extraído del token con el usuario real.
    private boolean isTokenUsernameMatchingUser(String token, String username){
        return extractUsername(token).equals(username);
    }

    // Comprueba si el token ha expirado comparando la fecha actual con la fecha de expiración.
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    // Busca en la base de datos si el token está activo y no se ha cerrado sesión.
    private boolean isTokenActive(String token){
        return tokenJpaRepository.findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())  // Si el token existe, devuelve true si no está en logout.
                .orElse(false);  // Si el token no existe en la base de datos, se considera inactivo.
    }

    // Comprueba si el refresh token está activo en la base de datos.
    private boolean isRefreshTokenActive(String token) {
        return tokenJpaRepository.findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);
    }

    // Extrae la fecha de expiración del token.
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrae cualquier dato (claim) del token usando una función que decide qué claim extraer.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);  // Obtiene todos los claims del token.
        return claimsResolver.apply(claims);  // Aplica la función dada para obtener el claim deseado.
    }

    // Obtiene todos los claims (datos) del token verificando la firma con la clave secreta.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // Verifica el token con la clave secreta.
                .build()
                .parseSignedClaims(token)  // Parsea el token y extrae los claims.
                .getPayload();
    }

    // Genera un refresh token con una fecha de expiración específica.
    public String generateRefreshToken(Customer customer, String expirationInMillis){
        long expireTimeInMillis = Long.parseLong(expirationInMillis);  // Convierte el tiempo de expiración en milisegundos.
        Date expirationDate = Date.from(Instant.now().plus(expireTimeInMillis, ChronoUnit.MILLIS));  // Calcula la fecha de expiración.

        return Jwts.builder()
                .subject(customer.getUsername())  // Guarda el username en el token.
                .claim("role", customer.getRole().name())  // Guarda el rol del usuario en el token.
                .issuedAt(new Date(System.currentTimeMillis()))  // Fecha de emisión del token.
                .expiration(expirationDate)  // Fecha de expiración del token.
                .signWith(getSigningKey())  // Firma el token con la clave secreta.
                .compact();  // Genera el token en formato compacto (string JWT).
                // Compact(): Permite iniciar sesión y reciba un token que prueba su identidad sin necesidad de enviar credenciales en cada solicitud
                //Incluye informarcion sobre los permisos de usuario, como su role o privilegios. E incluye una firma digital para asegurar su integridad.
    }

    // Obtiene la clave secreta para firmar y verificar los tokens.
    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(opositaWebProperties.getSecurity().getJwt().getSecretKey());  // Decodifica la clave secreta desde Base64.
        return Keys.hmacShaKeyFor(keyBytes);  // Genera una clave HMAC para firmar los tokens.
    }

}
