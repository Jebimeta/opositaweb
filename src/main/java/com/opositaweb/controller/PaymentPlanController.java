package com.opositaweb.controller;

import com.opositaweb.apifirst.api.PaymentPlanApiDelegate;
import com.opositaweb.domain.vo.PaymentPlanRequest;
import com.opositaweb.domain.vo.PaymentPlanResponse;
import com.opositaweb.repository.entities.PaymentPlan;
import com.opositaweb.service.paymentplan.PaymentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentPlanController implements PaymentPlanApiDelegate {

	private final PaymentPlanService paymentPlanService;

	private final ConversionService conversionService;

	@Override
	public ResponseEntity<PaymentPlanResponse> getPaymentPlanById(Integer id) {
		PaymentPlan paymentPlan = paymentPlanService.findById(id.longValue());
		PaymentPlanResponse paymentPlanResponse = conversionService.convert(paymentPlan, PaymentPlanResponse.class);
		return ResponseEntity.ok(paymentPlanResponse);
	}

	@Override
	public ResponseEntity<List<PaymentPlanResponse>> getPaymentPlans() {
		List<PaymentPlanResponse> paymentPlans = paymentPlanService.findAll()
			.stream()
			.map(paymentPlan -> conversionService.convert(paymentPlan, PaymentPlanResponse.class))
			.toList();
		return ResponseEntity.ok(paymentPlans);
	}

	@Override
	public ResponseEntity<PaymentPlanResponse> createPaymentPlan(PaymentPlanRequest paymentPlanRequest) {
		PaymentPlan savedPaymentPlan = paymentPlanService.save(paymentPlanRequest);
		PaymentPlanResponse paymentPlanResponse = conversionService.convert(savedPaymentPlan,
				PaymentPlanResponse.class);
		return ResponseEntity.ok(paymentPlanResponse);
	}

	@Override
	public ResponseEntity<Void> deletePaymentPlan(Integer id) {
		paymentPlanService.delete(id.longValue());
		return ResponseEntity.ok(null);
	}

	@Override
	public ResponseEntity<PaymentPlanResponse> updatePaymentPlan(Integer id, PaymentPlanRequest paymentPlanRequest) {
		PaymentPlan paymentPlan = conversionService.convert(paymentPlanRequest, PaymentPlan.class);
		PaymentPlan updatedPaymentPlan = paymentPlanService.update(paymentPlan);
		PaymentPlanResponse paymentPlanResponse = conversionService.convert(updatedPaymentPlan,
				PaymentPlanResponse.class);
		return ResponseEntity.ok(paymentPlanResponse);
	}

}
