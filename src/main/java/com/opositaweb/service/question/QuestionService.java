package com.opositaweb.service.question;

import com.opositaweb.repository.entities.Question;
import com.opositaweb.repository.enums.Theme;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

	List<Question> findAll();

	Question findById(Long id);

	Question save(Question question);

	Question update(Question question);

	void delete(Long id);

}
