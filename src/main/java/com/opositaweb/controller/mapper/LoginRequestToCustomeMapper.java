package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.LoginRequest;
import com.opositaweb.repository.entities.Customer;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface LoginRequestToCustomeMapper extends Converter<LoginRequest, Customer> {

    Customer convert(@NonNull LoginRequest source);
}
