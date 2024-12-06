package com.opositaweb.controller;

import com.opositaweb.apifirst.api.TestApiDelegate;
import com.opositaweb.domain.vo.TestRequest;
import com.opositaweb.domain.vo.TestResponse;
import com.opositaweb.repository.entities.Test;
import com.opositaweb.service.test.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController implements TestApiDelegate {

	private final TestService testService;

	private final ConversionService conversionService;

	@Override
	public ResponseEntity<List<TestResponse>> getTests() {
		List<TestResponse> tests = testService.findAll()
			.stream()
			.map(test -> conversionService.convert(test, TestResponse.class))
			.toList();
		return ResponseEntity.ok(tests);
	}

	@Override
	public ResponseEntity<TestResponse> getTestById(Integer id) {
		Test test = testService.findById(id.longValue());
		TestResponse testResponse = conversionService.convert(test, TestResponse.class);
		return ResponseEntity.ok(testResponse);
	}

	@Override
	public ResponseEntity<TestResponse> createTest(TestRequest testRequest) {
		Test test = conversionService.convert(testRequest, Test.class);
		Test savedTest = testService.save(test);
		TestResponse testResponse = conversionService.convert(savedTest, TestResponse.class);
		return ResponseEntity.ok(testResponse);
	}

	@Override
	public ResponseEntity<Void> deleteTest(Integer id) {
		testService.delete(id.longValue());
		return ResponseEntity.ok(null);
	}

	@Override
	public ResponseEntity<TestResponse> updateTest(Integer id, TestRequest testRequest) {
		Test test = conversionService.convert(testRequest, Test.class);
		Test updatedTest = testService.update(test);
		TestResponse testResponse = conversionService.convert(updatedTest, TestResponse.class);
		return ResponseEntity.ok(testResponse);
	}

}
