package com.opositaweb.service.test.impl;

import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import com.opositaweb.repository.entities.Test;
import com.opositaweb.repository.jpa.TestRepository;
import com.opositaweb.service.test.TestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

	private final TestRepository testRepository;

	@Transactional
	@Override
	public List<Test> findAll() {
		return testRepository.findAll();
	}

	@Transactional
	@Override
	public Test findById(Long id) {
		return testRepository.findById(id).orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_TEST_NOT_FOUND));
	}

	@Transactional
	@Override
	public Test findByName(String name) {
		return (Test) testRepository.findByName(name)
			.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_TEST_NOT_FOUND));
	}

	@Transactional
	@Override
	public Test save(Test test) {
		try {
			return testRepository.save(test);
		}
		catch (Exception e) {
			throw new BusinessException(AppErrorCode.ERROR_SAVE, e);
		}
	}

	@Transactional
	@Override
	public Test update(Test test) {
		Optional<Test> existingTest = testRepository.findById(test.getId());
		if (existingTest.isPresent()) {
			Test testToUpdate = existingTest.get();
			testToUpdate.setName(test.getName());
			return testRepository.save(test);
		}
		else {
			throw new BusinessException(AppErrorCode.ERROR_UPDATE);
		}
	}

	@Transactional
	@Override
	public void delete(Long id) {
		Optional<Test> test = testRepository.findById(id);
		if (test.isPresent()) {
			testRepository.delete(test.get());
		}
		else {
			throw new BusinessException(AppErrorCode.ERROR_DELETE);
		}
	}

}
