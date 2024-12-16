package com.opositaweb.repository.entities;

import com.opositaweb.repository.enums.Option;
import com.opositaweb.repository.enums.Theme;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "questions", nullable = false)
	private String questionStatement;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Option optionA;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Option optionB;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Option optionC;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Option optionD;

	@NotBlank
	@Column(nullable = false)
	private String answer;

	@NotBlank
	@Column(nullable = false)
	private String explanation;

	@Enumerated(EnumType.STRING)
	private Theme theme;

	@ManyToOne
	@JoinColumn(name = "test_id", nullable = false, unique = true)
	private Test test;

}