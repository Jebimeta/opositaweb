package com.opositaweb.service.payment;

import com.opositaweb.repository.entities.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

	List<Payment> findAll();

	Payment findById(Long id);

	Payment save(Payment payment);

	Payment update(Payment payment);

	void delete(Long id);

}
