package com.opositaweb.controller;

import com.opositaweb.apifirst.api.PaymentApiDelegate;
import com.opositaweb.domain.vo.PaymentRequest;
import com.opositaweb.domain.vo.PaymentResponse;
import com.opositaweb.repository.entities.Payment;
import com.opositaweb.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApiDelegate {

	private final PaymentService paymentService;

	private final ConversionService conversionService;

	@Override
	public ResponseEntity<PaymentResponse> getPaymentById(Integer id) {
		Payment payment = paymentService.findById(id.longValue());
		PaymentResponse paymentResponse = conversionService.convert(payment, PaymentResponse.class);
		return ResponseEntity.ok(paymentResponse);
	}

	@Override
	public ResponseEntity<List<PaymentResponse>> getPayments() {
		List<PaymentResponse> paymentResponses = paymentService.findAll()
			.stream()
			.map(payment -> conversionService.convert(payment, PaymentResponse.class))
			.toList();
		return ResponseEntity.ok(paymentResponses);
	}

	@Override
	public ResponseEntity<PaymentResponse> createPayment(PaymentRequest paymentRequest) {
		Payment payment = conversionService.convert(paymentRequest, Payment.class);
		Payment savedPayment = paymentService.save(payment);
		PaymentResponse paymentResponse = conversionService.convert(savedPayment, PaymentResponse.class);
		return ResponseEntity.ok(paymentResponse);
	}

	@Override
	public ResponseEntity<Void> deletePayment(Integer id) {
		paymentService.delete(id.longValue());
		return ResponseEntity.ok(null);
	}

	@Override
	public ResponseEntity<PaymentResponse> updatePayment(Integer id, PaymentRequest paymentRequest) {
		Payment payment = conversionService.convert(paymentRequest, Payment.class);
		Payment updatedPayment = paymentService.update(payment);
		PaymentResponse paymentResponse = conversionService.convert(updatedPayment, PaymentResponse.class);
		return ResponseEntity.ok(paymentResponse);
	}

}
