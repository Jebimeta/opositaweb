package com.opositaweb.service.payment.impl;

import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import com.opositaweb.repository.entities.Payment;
import com.opositaweb.repository.jpa.PaymentRepository;
import com.opositaweb.service.payment.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	@Transactional
	@Override
	public List<Payment> findAll() {
		return paymentRepository.findAll();
	}

	@Transactional
	@Override
	public Payment findById(Long id) {
		Optional<Payment> payment = paymentRepository.findById(id);
		if (payment.isPresent()) {
			return payment.get();
		}
		else {
			throw new BusinessException(AppErrorCode.ERROR_PAYMENT_NOT_FOUND);
		}
	}

	@Transactional
	@Override
	public Payment save(Payment payment) {
		try{
			return paymentRepository.save(payment);
		} catch (Exception e) {
			throw new BusinessException(AppErrorCode.ERROR_SAVE, e);
		}

	}

	@Transactional
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
			throw new BusinessException(AppErrorCode.ERROR_UPDATE);
		}
	}

	@Transactional
	@Override
	public void delete(Long id) {
		Optional<Payment> payment = paymentRepository.findById(id);
		if (payment.isPresent()) {
			paymentRepository.delete(payment.get());
		}
		else {
			throw new BusinessException(AppErrorCode.ERROR_DELETE);
		}
	}
}
