package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.PaymentResponse;
import com.opositaweb.repository.entities.Payment;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface PaymentToPaymentResponseMapper extends Converter<Payment, PaymentResponse> {

	PaymentResponse convert(Payment source);

}
