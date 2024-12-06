package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.QuestionRequest;
import com.opositaweb.repository.entities.Question;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface QuestionRequestToQuestionMapper extends Converter<QuestionRequest, Question> {

	Question convert(@NonNull QuestionRequest source);

}
