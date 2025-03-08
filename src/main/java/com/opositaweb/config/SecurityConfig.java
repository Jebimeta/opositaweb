package com.opositaweb.config;

import com.opositaweb.filter.JwtAuthenticationFilter;
import com.opositaweb.service.auth.impl.UserDetailsServiceImpl;
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

import static com.opositaweb.config.enums.OpositaWebApiEndpoints.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final UserDetailsServiceImpl userDetailsServiceImpl;

	private final CustomLogoutHandler customLogoutHandler;

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
						.requestMatchers(SWAGGER_UI_HTML.getUrl(), SWAGGER_UI.getUrl(), API_DOCS.getUrl()) // Define patrones de URL.
						.permitAll()
						.requestMatchers(LOGIN_URL.getUrl(), REGISTER_URL.getUrl(), REFRESH_TOKEN_URL.getUrl(), VERIFY_TOKEN_URL.getUrl(),
								SEND_EMAIL_URL.getUrl(), PAYMENT_PLAN_LIST_URL.getUrl(), PAYMENT_PLAN_SAVE_URL.getUrl(), PDF_LIST_URL.getUrl(),
								PDF_BY_ID_URL.getUrl(),  TEST_LIST_URL.getUrl(), PDF_LIST_URL.getUrl(), SEND_EMAIL_URL.getUrl())
						.permitAll()// Permite acceso sin autenticación.
						.requestMatchers(USERS_LIST_URL.getUrl(), USER_BY_ID_URL.getUrl(), USER_BY_EMAIL_URL.getUrl(), USER_BY_USERNAME_URL.getUrl(),
								USER_UPDATE_URL.getUrl(), USER_DELETE_URL.getUrl(), PAYMENT_PLAN_BY_ID_URL.getUrl(), PAYMENT_PLAN_SAVE_URL.getUrl(),
								PAYMENT_PLAN_DELETE_URL.getUrl(), QUESTION_BY_ID_URL.getUrl(), QUESTION_SAVE_URL.getUrl(), QUESTION_UPDATE_URL.getUrl(),
								QUESTION_DELETE_URL.getUrl(), TEST_SAVE_URL.getUrl(), TEST_UPDATE_URL.getUrl(), TEST_DELETE_URL.getUrl(), PDF_SAVE_URL.getUrl(),
								PDF_UPDATE_URL.getUrl(), PDF_DELETE_URL.getUrl(), PAYMENT_LIST_URL.getUrl(), PAYMENT_BY_ID_URL.getUrl(), PAYMENT_SAVE_URL.getUrl(),
								PAYMENT_UPDATE_URL.getUrl(), PAYMENT_DELETE_URL.getUrl())
						.hasRole("ROLE_ADMIN") // Restringe acceso a usuarios con rol ADMIN.
						.requestMatchers(LOGOUT_URL.getUrl(), AUTHENTICATED_USER_INFO_URL.getUrl(), QUESTION_LIST_URL.getUrl(), TEST_BY_ID_URL.getUrl(), PDF_BY_ID_URL.getUrl()) // Define patrones adicionales (puede optimizarse).
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
