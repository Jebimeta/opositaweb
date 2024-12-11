package com.opositaweb.service.paymentplan;

import com.opositaweb.domain.vo.PaymentPlanRequest;
import com.opositaweb.repository.entities.PaymentPlan;

import java.util.List;
import java.util.Optional;

public interface PaymentPlanService {

	List<PaymentPlan> findAll();

	PaymentPlan findById(Long id);

	PaymentPlan save(PaymentPlanRequest paymentPlanRequest);

	PaymentPlan update(PaymentPlan paymentPlan);

	void delete(Long id);

}
