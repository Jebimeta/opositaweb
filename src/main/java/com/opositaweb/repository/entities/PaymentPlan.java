package com.opositaweb.repository.entities;

import com.opositaweb.repository.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_plan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_type", nullable = false)
	private PaymentType paymentType;

	@Column(name = "price", nullable = false)
	private Float price;

}