package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.CustomerResponse;
import com.opositaweb.repository.entities.Customer;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface CustomerToCustomerResponseMapper extends Converter<Customer, CustomerResponse> {

	CustomerResponse convert(Customer source);

}
