package com.opositaweb.repository.entities;

import com.opositaweb.repository.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotBlank(message = "El nombre no se puede quedar vacío")
	@Column(name = "name", nullable = false)
	private String name;

	@NotBlank(message = "Los apellidos no se puede quedar vacío")
	@Column(name = "last_names", nullable = false)
	private String lastNames;

	@NotBlank(message = "El DNI no se puede quedar vacío")
	@Column(name = "dni", nullable = false)
	private String dni;

	@NotBlank(message = "El Email no se puede quedar vacío")
	@Column(name = "email", nullable = false)
	private String email;

	@NotBlank(message = "El Password no se puede quedar vacío")
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "telephone", nullable = true)
	private String telephone;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Rol role;

	private boolean status;

	private boolean subscriptionStatus;

	private String verificationToken;

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return getName();
	}

}
