package com.opositaweb.service.auth.util;

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

	private final OpositaWebProperties properties;

	private final TokenJpaRepository tokenRepository;

	// Extrae el nombre de usuario ("subject") de un token JWT.
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Valida si un token es válido para un usuario dado.
	 * Comprueba que el nombre de usuario coincida, que el token no haya expirado y que esté activo.
	 */
	public boolean isValid(String token, UserDetails user) {
		return isTokenUsernameMatchingUser(token, user.getUsername())
				&& !isTokenExpired(token)
				&& isTokenActive(token);
	}

	/**
	 * Valida si un token de actualización (refresh token) es válido para un cliente.
	 * Realiza verificaciones similares a isValid pero enfocado en refresh tokens.
	 */
	public boolean isValidRefreshToken(String token, Customer customer) {
		return isTokenUsernameMatchingUser(token, customer.getUsername())
				&& !isTokenExpired(token)
				&& isRefreshTokenActive(token);
	}

	// Comprueba si el nombre de usuario en el token coincide con el esperado.
	private boolean isTokenUsernameMatchingUser(String token, String username) {
		return extractUsername(token).equals(username);
	}

	// Comprueba si el token ya ha expirado basándose en la fecha de expiración.
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Verifica si el token está activo buscando en el repositorio y comprobando si está marcado como no cerrado ("loggedOut").
	private boolean isTokenActive(String token) {
		return tokenRepository.findByAccessToken(token)
				.map(t -> !t.isLoggedOut())
				.orElse(false);
	}

	// Similar a isTokenActive pero para tokens de actualización (refresh tokens).
	private boolean isRefreshTokenActive(String token) {
		return tokenRepository.findByRefreshToken(token)
				.map(t -> !t.isLoggedOut())
				.orElse(false);
	}

	// Extrae la fecha de expiración de un token JWT.
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Extrae una reclamación específica del token usando una función (resolver).
	 * Esto permite obtener diferentes valores del cuerpo del token.
	 */
	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}

	// Extrae todas las reclamaciones del token desencriptándolo con la clave secreta.
	private Claims extractAllClaims(String token) {
		return Jwts.parser() // Crea un parser JWT.
				.verifyWith(getSigninKey()) // Configura la clave secreta.
				.build() // Construye el parser.
				.parseSignedClaims(token) // Analiza el token firmado.
				.getPayload(); // Obtiene el payload del token (reclamaciones).
	}

	/**
	 * Genera un token de acceso (access token) para un cliente dado con una expiración específica.
	 */
	public String generateAccessToken(Customer customer) {
		return generateToken(customer, properties.getSecurity().getJwt().getAccessTokenExpiration());
	}

	/**
	 * Genera un token de actualización (refresh token) para un cliente dado con una expiración específica.
	 */
	public String generateRefreshToken(Customer customer) {
		return generateToken(customer, properties.getSecurity().getJwt().getRefreshTokenExpiration());
	}

	/**
	 * Método genérico para generar un token (access o refresh).
	 * Configura el subject, los roles, la fecha de emisión y expiración, y firma el token.
	 */
	private String generateToken(Customer customer, String expirationInMillis) {
		long expireTimeInMillis = Long.parseLong(expirationInMillis);
		Date expirationDate = Date.from(Instant.now().plus(expireTimeInMillis, ChronoUnit.MILLIS));

		return Jwts.builder()
				.subject(customer.getUsername()) // Asigna el nombre de usuario como subject.
				.claim("role", customer.getRole().name()) // Incluye el rol del cliente.
				.issuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión.
				.expiration(expirationDate) // Fecha de expiración.
				.signWith(getSigninKey()) // Firma el token con la clave secreta.
				.compact(); // Genera el token en formato compacto.
	}

	/**
	 * Obtiene la clave secreta usada para firmar los tokens.
	 * Decodifica la clave desde Base64 y la convierte en una clave HMAC-SHA.
	 */
	private SecretKey getSigninKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(properties.getSecurity().getJwt().getSecretKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
