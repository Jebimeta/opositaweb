package com.opositaweb.service.test;

import com.opositaweb.repository.entities.Test;

import java.util.List;

public interface TestService {

	List<Test> findAll();

	Test findById(Long id);

	Test findByName(String name);

	Test save(Test test);

	Test update(Test test);

	void delete(Long id);

}
