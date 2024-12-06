package com.opositaweb.repository.entities;

import com.opositaweb.repository.enums.Theme;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "test")
public class Test {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotBlank
	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "theme_id", nullable = true)
	private Theme theme;

	@OneToMany(mappedBy = "test")
	private List<Question> questions;

}
