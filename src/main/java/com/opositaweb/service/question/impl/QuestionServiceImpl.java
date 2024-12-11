package com.opositaweb.service.question.impl;

import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import com.opositaweb.repository.entities.Question;
import com.opositaweb.repository.enums.Theme;
import com.opositaweb.repository.jpa.QuestionRepository;
import com.opositaweb.service.question.QuestionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

	private final QuestionRepository questionRepository;

	@Transactional
	@Override
	public List<Question> findAll() {
		return questionRepository.findAll();
	}

	@Transactional
	@Override
	public Question findById(Long id) {
		Optional<Question> question = questionRepository.findById(id);
		return question.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_QUESTION_NOT_FOUND));
	}

	@Transactional
	@Override
	public Question save(Question question) {
		try{
			return questionRepository.save(question);
		} catch (Exception e) {
			throw new BusinessException((AppErrorCode.ERROR_SAVE));
		}
	}

	@Transactional
	@Override
	public Question update(Question question) {
		Optional<Question> existingQuestion = questionRepository.findById(question.getId());
		if (existingQuestion.isPresent()){
			Question updatedQuestion = existingQuestion.get();
			updatedQuestion.setQuestionStatement(question.getQuestionStatement());
			updatedQuestion.setOptionA(question.getOptionA());
			updatedQuestion.setOptionB(question.getOptionB());
			updatedQuestion.setOptionC(question.getOptionC());
			updatedQuestion.setOptionD(question.getOptionD());
			updatedQuestion.setAnswer(question.getAnswer());
			updatedQuestion.setExplanation(question.getExplanation());
			updatedQuestion.setTheme(question.getTheme());
			updatedQuestion.setTest(question.getTest());
			return questionRepository.save(updatedQuestion);
		}else{
			throw new BusinessException(AppErrorCode.ERROR_UPDATE);
		}
	}

	@Transactional
	@Override
	public void delete(Long id) {
		Optional<Question> question = questionRepository.findById(id);
		if (question.isPresent()){
			questionRepository.delete(question.get());
		}else{
			throw new BusinessException(AppErrorCode.ERROR_DELETE);
		}
	}
}