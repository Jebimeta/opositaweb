package com.opositaweb.service.paymentplan;

import com.opositaweb.repository.entities.PaymentPlan;

import java.util.List;
import java.util.Optional;

public interface PaymentPlanService {

	List<PaymentPlan> findAll();

	Optional<PaymentPlan> findById(Long id);

	Optional<PaymentPlan> findByName(String name);

	PaymentPlan save(PaymentPlan paymentPlan);

	PaymentPlan update(PaymentPlan paymentPlan);

	void delete(Long id);

}
