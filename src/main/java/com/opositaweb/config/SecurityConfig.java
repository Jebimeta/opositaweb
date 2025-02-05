package com.opositaweb.config;

import com.opositaweb.config.filter.JwtAuthenticationFilter;
import com.opositaweb.service.auth.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	// Servicio personalizado para cargar detalles de usuario.
	private final UserDetailsServiceImpl userDetailsServiceImpl;

	// Manejador personalizado para acel cierre de sesión.
	private final CustomLogoutHandler customLogoutHandler;

	// Filtro para la autenticación basada en JWT.
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	/**
	 * Configura la cadena de seguridad de Spring Security.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		configureCsrf(http); // Desactiva la protección CSRF.
		configureAuthorization(http); // Configura las reglas de autorización.
		configureUserDetailsService(http); // Configura el servicio de detalles de usuario.
		configureSessionManagement(http); // Configura la gestión de sesiones.
		configureFilter(http); // Añade el filtro de JWT antes del filtro de autenticación por usuario/contraseña.
		configureExceptionHandling(http); // Configura el manejo de excepciones.
		configureLogout(http); // Configura el comportamiento del cierre de sesión.
		return http.build();
	}

	/**
	 * Desactiva la protección contra CSRF (Cross-Site Request Forgery).
	 */
	private void configureCsrf(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
	}

	/**
	 * Configura las reglas de autorización para las solicitudes HTTP.
	 */
	private void configureAuthorization(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				registry -> registry
						.requestMatchers("/**") // Define patrones de URL.
						.permitAll() // Permite acceso sin autenticación.
						.requestMatchers("/**") // Define patrones adicionales (puede optimizarse).
						.hasRole("ROLE_ADMIN") // Restringe acceso a usuarios con rol ADMIN.
						.requestMatchers("/**") // Define patrones adicionales (puede optimizarse).
						.authenticated() // Requiere autenticación para estas rutas.
		);
	}

	/**
	 * Configura el servicio que proporciona los detalles de los usuarios.
	 */
	private void configureUserDetailsService(HttpSecurity http) throws Exception {
		http.userDetailsService(userDetailsServiceImpl);
	}

	/**
	 * Configura la política de gestión de sesiones.
	 * En este caso, se usa una política STATELESS, adecuada para aplicaciones basadas en JWT.
	 */
	private void configureSessionManagement(HttpSecurity http) throws Exception {
		http.sessionManagement(
				sessionManagement -> sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);
	}

	/**
	 * Añade el filtro de autenticación JWT antes del filtro estándar de autenticación por usuario y contraseña.
	 */
	private void configureFilter(HttpSecurity http) {
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	/**
	 * Configura el manejo de excepciones relacionadas con la seguridad.
	 */
	private void configureExceptionHandling(HttpSecurity http) throws Exception {
		http.exceptionHandling(exception -> exception
				.accessDeniedHandler(
						(request, response, accessDeniedException)
								-> response.setStatus(HttpStatus.FORBIDDEN.value())) // Devuelve 403 si se niega el acceso.
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // Devuelve 401 si no está autenticado.
		);
	}

	/**
	 * Configura el comportamiento del cierre de sesión.
	 */
	private void configureLogout(HttpSecurity http) throws Exception {
		http.logout(logout -> logout.logoutUrl("/logout") // URL para cerrar sesión.
				.addLogoutHandler(customLogoutHandler) // Manejador personalizado de cierre de sesión.
				.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())); // Limpia el contexto de seguridad.
	}

	/**
	 * Bean para codificar contraseñas usando BCrypt.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Bean para gestionar la autenticación, permitiendo su uso en otros servicios.
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * Bean para eliminar el prefijo "ROLE_" de los roles de usuario.
	 * Esto simplifica las comprobaciones de roles en las políticas de autorización.
	 */
	@Bean
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("");
	}
}
