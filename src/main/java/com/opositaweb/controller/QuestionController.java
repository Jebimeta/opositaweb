package com.opositaweb.controller;

import com.opositaweb.apifirst.api.QuestionApiDelegate;
import com.opositaweb.domain.vo.QuestionRequest;
import com.opositaweb.domain.vo.QuestionResponse;
import com.opositaweb.repository.entities.Question;
import com.opositaweb.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController implements QuestionApiDelegate {

	private final QuestionService questionService;

	private final ConversionService conversionService;

	@Override
	public ResponseEntity<QuestionResponse> getQuestionById(Integer id) {
		Question question = questionService.findById(id.longValue())
			.orElseThrow(() -> new RuntimeException("Question not found"));
		QuestionResponse questionResponse = conversionService.convert(question, QuestionResponse.class);
		return ResponseEntity.ok(questionResponse);
	}

	@Override
	public ResponseEntity<List<QuestionResponse>> getQuestions() {
		List<QuestionResponse> questions = questionService.findAll()
			.stream()
			.map(question -> conversionService.convert(question, QuestionResponse.class))
			.toList();
		return ResponseEntity.ok(questions);
	}

	@Override
	public ResponseEntity<QuestionResponse> createQuestion(QuestionRequest questionRequest) {
		Question question = conversionService.convert(questionRequest, Question.class);
		Question savedQuestion = questionService.save(question);
		QuestionResponse questionResponse = conversionService.convert(savedQuestion, QuestionResponse.class);
		return ResponseEntity.ok(questionResponse);
	}

	@Override
	public ResponseEntity<Void> deleteQuestion(Integer id) {
		questionService.delete(id.longValue());
		return ResponseEntity.ok(null);
	}

	@Override
	public ResponseEntity<QuestionResponse> updateQuestion(Integer id, QuestionRequest questionRequest) {
		Question question = conversionService.convert(questionRequest, Question.class);
		Question updatedQuestion = questionService.update(question);
		QuestionResponse questionResponse = conversionService.convert(updatedQuestion, QuestionResponse.class);
		return ResponseEntity.ok(questionResponse);
	}

}
