package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.CustomerRequest;
import com.opositaweb.repository.entities.Customer;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface CustomerRequestToCustomerMapper extends Converter<CustomerRequest, Customer> {

	Customer convert(@NonNull CustomerRequest source);

}
