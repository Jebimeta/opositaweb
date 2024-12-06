package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.PaymentPlanResponse;
import com.opositaweb.repository.entities.PaymentPlan;
import org.springframework.core.convert.converter.Converter;

public interface PaymentPlanToPaymentPlanResponseMapper extends Converter<PaymentPlan, PaymentPlanResponse> {

    PaymentPlanResponse convert(PaymentPlan source);
}
