package com.opositaweb.service.question.impl;

import com.opositaweb.repository.entities.Question;
import com.opositaweb.repository.enums.Theme;
import com.opositaweb.repository.jpa.QuestionRepository;
import com.opositaweb.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

	private final QuestionRepository questionRepository;

	@Override
	public List<Question> findAll() {
		return questionRepository.findAll();
	}

	@Override
	public Optional<Question> findById(Long id) {
		return questionRepository.findById(id);
	}

	@Override
	public Question save(Question question) {
		return questionRepository.save(question);
	}

	@Override
	public Question update(Question question) {
		return questionRepository.save(question);
	}

	@Override
	public void delete(Long id) {
		questionRepository.deleteById(id);
	}

}