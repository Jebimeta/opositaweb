package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.TestResponse;
import com.opositaweb.repository.entities.Test;
import org.springframework.core.convert.converter.Converter;

public interface TestToTestResposeMapper extends Converter<Test, TestResponse> {

	TestResponse convert(Test source);

}
