package com.opositaweb.config.filter;

import com.opositaweb.service.auth.UserDetailsServiceImpl;
import com.opositaweb.service.auth.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT personalizado que se ejecuta una vez por solicitud.
 * Este filtro valida el token JWT y establece la autenticación del usuario en el contexto de seguridad.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	// Nombre del encabezado donde se envía el token de autenticación.
	private static final String AUTH_HEADER = "Authorization";

	// Prefijo utilizado en el encabezado de autorización para los tokens JWT.
	private static final String BEARER_PREFIX = "Bearer ";

	// Servicio para manejar la lógica relacionada con los tokens JWT.
	private final JwtService jwtService;

	// Servicio personalizado para cargar detalles del usuario.
	private final UserDetailsServiceImpl userDetailsServiceImpl;

	/**
	 * Lógica principal del filtro. Valida el token JWT y autentica al usuario si es válido.
	 *
	 * @param request  La solicitud HTTP entrante.
	 * @param response La respuesta HTTP saliente.
	 * @param filterChain La cadena de filtros a ejecutar.
	 * @throws ServletException Si ocurre un error en el procesamiento del filtro.
	 * @throws IOException Si ocurre un error de entrada/salida.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// Extrae el token del encabezado de la solicitud.
		String token = extractToken(request.getHeader(AUTH_HEADER));

		// Verifica si el token es válido y si no hay una autenticación previa en el contexto.
		if (token != null && !isAuthenticationPresent()) {
			authenticateUser(token, request);
		}

		// Continúa con el resto de la cadena de filtros.
		filterChain.doFilter(request, response);
	}

	/**
	 * Extrae el token JWT del encabezado de autorización.
	 *
	 * @param authHeader El valor del encabezado "Authorization".
	 * @return El token JWT si está presente y tiene el prefijo "Bearer", de lo contrario, null.
	 */
	private String extractToken(String authHeader) {
		if (isBearerToken(authHeader)) {
			return authHeader.substring(BEARER_PREFIX.length());
		}
		return null;
	}

	/**
	 * Verifica si el encabezado contiene un token JWT con el prefijo "Bearer".
	 *
	 * @param authHeader El valor del encabezado "Authorization".
	 * @return true si el encabezado comienza con el prefijo "Bearer", de lo contrario, false.
	 */
	private boolean isBearerToken(String authHeader) {
		return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
	}

	/**
	 * Verifica si ya existe una autenticación en el contexto de seguridad.
	 *
	 * @return true si ya hay una autenticación presente, de lo contrario, false.
	 */
	private boolean isAuthenticationPresent() {
		return SecurityContextHolder.getContext().getAuthentication() != null;
	}

	/**
	 * Autentica al usuario utilizando el token JWT.
	 *
	 * @param token   El token JWT extraído de la solicitud.
	 * @param request La solicitud HTTP.
	 */
	private void authenticateUser(String token, HttpServletRequest request) {
		// Extrae el nombre de usuario del token.
		String extractedUsername = jwtService.extractUsername(token);

		// Si el nombre de usuario es válido y el token también lo es, autentica al usuario.
		if (extractedUsername != null && jwtService.isValid(token, userDetailsServiceImpl.loadUserByUsername(extractedUsername))) {
			setAuthentication(userDetailsServiceImpl.loadUserByUsername(extractedUsername), request);
		}
	}

	/**
	 * Establece la autenticación del usuario en el contexto de seguridad.
	 *
	 * @param userDetails Los detalles del usuario autenticado.
	 * @param request     La solicitud HTTP.
	 */
	private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
		// Crea un token de autenticación con los detalles del usuario y sus roles.
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		// Asocia los detalles de la solicitud al token de autenticación.
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		// Establece la autenticación en el contexto de seguridad.
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}
