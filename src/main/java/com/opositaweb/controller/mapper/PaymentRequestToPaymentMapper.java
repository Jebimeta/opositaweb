package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.PaymentRequest;
import com.opositaweb.repository.entities.Payment;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface PaymentRequestToPaymentMapper extends Converter<PaymentRequest, Payment> {

	Payment convert(PaymentRequest source);

}
