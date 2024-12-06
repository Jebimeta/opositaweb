package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.PaymentPlanRequest;
import com.opositaweb.repository.entities.PaymentPlan;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface PaymentPlanRequestToPaymentPlanMapper extends Converter<PaymentPlanRequest, PaymentPlan> {

    PaymentPlan convert(@NonNull PaymentPlanRequest source);
}
