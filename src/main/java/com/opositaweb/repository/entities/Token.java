package com.opositaweb.repository.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "token")
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "is_logged_out")
	private boolean loggedOut;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Customer customer;

}
