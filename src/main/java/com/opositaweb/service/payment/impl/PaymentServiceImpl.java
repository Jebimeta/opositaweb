package com.opositaweb.service.payment.impl;

import com.opositaweb.repository.entities.Payment;
import com.opositaweb.repository.jpa.PaymentRepository;
import com.opositaweb.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	@Override
	public List<Payment> findAll() {
		return paymentRepository.findAll();
	}

	@Override
	public Optional<Payment> findByPaymentMethod(String paymentMethod) {
		Optional<Payment> payment = paymentRepository.findByPaymentMethod(paymentMethod);
		if (payment.isPresent()) {
			return payment;
		}
		else {
			throw new RuntimeException("Payment not found");
		}
	}

	@Override
	public Payment findById(Long id) {
		Optional<Payment> payment = paymentRepository.findById(id);
		if (payment.isPresent()) {
			return payment.get();
		}
		else {
			throw new RuntimeException("Payment not found");
		}
	}

	@Override
	public Payment save(Payment payment) {
		return paymentRepository.save(payment);
	}

	@Override
	public Payment update(Payment payment) {
		Optional<Payment> paymentOptional = paymentRepository.findById(payment.getId());
		if (paymentOptional.isPresent()) {
			Payment paymentToUpdate = paymentOptional.get();
			paymentToUpdate.setCustomer(payment.getCustomer());
			paymentToUpdate.setPaymentDate(payment.getPaymentDate());
			paymentToUpdate.setPaymentPlan(payment.getPaymentPlan());
			paymentToUpdate.setAmount(payment.getAmount());
			return paymentRepository.save(paymentToUpdate);
		}
		else {
			throw new RuntimeException("Payment not found");
		}
	}

	@Override
	public void delete(Long id) {
		Optional<Payment> payment = paymentRepository.findById(id);
		if (payment.isPresent()) {
			paymentRepository.delete(payment.get());
		}
		else {
			throw new RuntimeException("Payment not found");
		}
	}

}
