package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.TestRequest;
import com.opositaweb.repository.entities.Test;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public interface TestRequestToTestMapper extends Converter<TestRequest, Test> {

	Test convert(@NonNull TestRequest source);

}
