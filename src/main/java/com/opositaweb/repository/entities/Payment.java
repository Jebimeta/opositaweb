// Payment.java
package com.opositaweb.repository.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

	@Column(name = "payment_date", nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDate paymentDate;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "payment_plan_id", nullable = false)
	private PaymentPlan paymentPlan;

	@Column(name = "subscription_start_date", nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDate subscriptionStartDate;

}
