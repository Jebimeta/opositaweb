package com.opositaweb.service.paymentplan.impl;

import com.opositaweb.domain.vo.PaymentPlanRequest;
import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import com.opositaweb.repository.entities.PaymentPlan;
import com.opositaweb.repository.jpa.PaymentPlanRepository;
import com.opositaweb.service.paymentplan.PaymentPlanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentPlanServiceImpl implements PaymentPlanService {

	private final PaymentPlanRepository paymentPlanRepository;

	@Transactional
	@Override
	public List<PaymentPlan> findAll() {
		return paymentPlanRepository.findAll();
	}

	@Transactional
	@Override
	public PaymentPlan findById(Long id) {
		Optional<PaymentPlan> paymentPlan = paymentPlanRepository.findById(id);
		return paymentPlan.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_PAYMENTPLAN_NOT_FOUND));
	}

	@Transactional
	@Override
	public PaymentPlan save(PaymentPlanRequest paymentPlanRequest) {
		return null;
	}

	@Transactional
	@Override
	public PaymentPlan update(PaymentPlan paymentPlan) {
		Optional<PaymentPlan> paymentPlanOptional = paymentPlanRepository.findById(paymentPlan.getId());
		if (paymentPlanOptional.isPresent()) {
			PaymentPlan paymentPlanUpdate = paymentPlanOptional.get();
			paymentPlanUpdate.setPaymentType(paymentPlan.getPaymentType());
			paymentPlanUpdate.setPrice(paymentPlan.getPrice());
			return paymentPlanRepository.save(paymentPlanUpdate);
		}
		else {
			throw new BusinessException(AppErrorCode.ERROR_UPDATE);
		}
	}

	@Transactional
	@Override
	public void delete(Long id) {
		Optional<PaymentPlan> paymentPlan = paymentPlanRepository.findById(id);
		if (paymentPlan.isPresent()) {
			paymentPlanRepository.delete(paymentPlan.get());
		}
		else {
			throw new BusinessException(AppErrorCode.ERROR_DELETE);
		}
	}

}
