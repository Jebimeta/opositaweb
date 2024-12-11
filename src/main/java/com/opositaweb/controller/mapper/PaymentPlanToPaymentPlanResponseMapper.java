package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.PaymentPlanResponse;
import com.opositaweb.repository.entities.PaymentPlan;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface PaymentPlanToPaymentPlanResponseMapper extends Converter<PaymentPlan, PaymentPlanResponse> {

	PaymentPlanResponse convert(PaymentPlan source);

}
