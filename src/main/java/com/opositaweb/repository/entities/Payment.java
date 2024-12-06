// Payment.java
package com.opositaweb.repository.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "amount", nullable = false)
	private Float amount;

	@Column(name = "payment_date", nullable = false)
	private String paymentDate;

	@Column(name = "payment_method", nullable = false)
	private String paymentMethod;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "payment_plan_id", nullable = false)
	private PaymentPlan paymentPlan;

}
