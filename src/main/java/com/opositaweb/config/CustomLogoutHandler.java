package com.opositaweb.config;

import com.opositaweb.repository.jpa.TokenJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Clase personalizada para manejar el proceso de cierre de sesión.
 * Esta clase implementa la interfaz {@link LogoutHandler} de Spring Security.
 */
@RequiredArgsConstructor
@Configuration
public class CustomLogoutHandler implements LogoutHandler {

	// Prefijo utilizado en los encabezados de autorización para los tokens JWT.
	private static final String BEARER_PREFIX = "Bearer ";

	// Longitud del prefijo Bearer.
	private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

	// Repositorio JPA para gestionar los tokens almacenados.
	private final TokenJpaRepository tokenRepository;

	/**
	 * Lógica principal del cierre de sesión.
	 * Este método se ejecuta cuando un usuario realiza un logout.
	 *
	 * @param request        La solicitud HTTP.
	 * @param response       La respuesta HTTP.
	 * @param authentication La información de autenticación del usuario (puede ser nula).
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// Extrae el token del encabezado de la solicitud.
		String token = extractTokenFromRequest(request);

		// Si se encuentra un token válido, se invalida.
		if (token != null)
			invalidateToken(token);
	}

	/**
	 * Extrae el token JWT del encabezado de autorización de la solicitud HTTP.
	 *
	 * @param request La solicitud HTTP.
	 * @return El token JWT si está presente, de lo contrario, null.
	 */
	private String extractTokenFromRequest(HttpServletRequest request) {
		// Obtiene el encabezado "Authorization" de la solicitud.
		String authHeader = getAuthorizationHeader(request);

		// Verifica si el encabezado contiene un token con el prefijo "Bearer".
		return isBearerToken(authHeader) ? extractBearerToken(authHeader) : null;
	}

	/**
	 * Obtiene el encabezado de autorización de la solicitud HTTP.
	 *
	 * @param request La solicitud HTTP.
	 * @return El valor del encabezado "Authorization", o null si no está presente.
	 */
	private String getAuthorizationHeader(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	/**
	 * Verifica si el encabezado de autorización contiene un token con el prefijo "Bearer".
	 *
	 * @param authHeader El encabezado de autorización.
	 * @return true si el encabezado comienza con el prefijo "Bearer", de lo contrario, false.
	 */
	private boolean isBearerToken(String authHeader) {
		return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
	}

	/**
	 * Extrae el token JWT eliminando el prefijo "Bearer" del encabezado de autorización.
	 *
	 * @param authHeader El encabezado de autorización.
	 * @return El token JWT sin el prefijo "Bearer".
	 */
	private String extractBearerToken(String authHeader) {
		return authHeader.substring(BEARER_PREFIX_LENGTH);
	}

	/**
	 * Invalida un token JWT marcándolo como cerrado en la base de datos.
	 *
	 * @param token El token JWT a invalidar.
	 */
	private void invalidateToken(String token) {
		// Busca el token en la base de datos utilizando el repositorio.
		tokenRepository.findByAccessToken(token).ifPresent(storedToken -> {
			// Marca el token como "logged out".
			storedToken.setLoggedOut(true);

			// Guarda los cambios en la base de datos.
			tokenRepository.save(storedToken);
		});
	}
}
