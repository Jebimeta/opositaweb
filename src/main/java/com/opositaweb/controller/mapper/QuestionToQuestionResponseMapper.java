package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.QuestionResponse;
import com.opositaweb.repository.entities.Question;
import org.springframework.core.convert.converter.Converter;

public interface QuestionToQuestionResponseMapper extends Converter<Question, QuestionResponse> {

	QuestionResponse convert(Question source);

}
