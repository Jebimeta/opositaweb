package com.opositaweb.service.auth;

import com.opositaweb.repository.entities.Customer;
import com.opositaweb.service.user.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final CustomerService customerService; // Servicio para buscar usuarios en la base de datos.

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 1. Busca al cliente en la base de datos usando su correo electrónico.
		Customer customer = customerService.findByEmail(username);

		// 2. Convierte el rol del cliente a un GrantedAuthority (requerido por Spring Security).
		List<GrantedAuthority> authorities = Collections
				.singletonList(new SimpleGrantedAuthority("ROLE" + customer.getRole().name()));

		// 3. Retorna un objeto User de Spring Security con email, contraseña y roles.
		return new User(customer.getEmail(), customer.getPassword(), authorities);
	}
}

