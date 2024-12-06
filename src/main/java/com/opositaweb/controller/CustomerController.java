package com.opositaweb.controller;

import com.opositaweb.apifirst.api.CustomerApiDelegate;
import com.opositaweb.domain.vo.CustomerRequest;
import com.opositaweb.domain.vo.CustomerResponse;
import com.opositaweb.repository.entities.Customer;
import com.opositaweb.service.user.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerApiDelegate {

	private final CustomerService customerService;

	private final ConversionService conversionService;

	@Override
	public ResponseEntity<List<CustomerResponse>> getUsers() {
		List<CustomerResponse> customerResponses = customerService.findAll()
			.stream()
			.map(customer -> conversionService.convert(customer, CustomerResponse.class))
			.toList();
		return ResponseEntity.ok(customerResponses);
	}

	@Override
	public ResponseEntity<CustomerResponse> getUserByDni(String dni) {
		Customer customer = customerService.findByDni(dni)
			.orElseThrow(() -> new RuntimeException("Customer not found"));
		CustomerResponse customerResponse = conversionService.convert(customer, CustomerResponse.class);
		return ResponseEntity.ok(customerResponse);
	}

	@Override
	public ResponseEntity<CustomerResponse> getUserByEmail(String email) {
		Customer customer = customerService.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("Customer not found"));
		CustomerResponse customerResponse = conversionService.convert(customer, CustomerResponse.class);
		return ResponseEntity.ok(customerResponse);
	}

	@Override
	public ResponseEntity<CustomerResponse> getUserById(Integer id) {
		Customer customer = customerService.findById(id.longValue())
			.orElseThrow(() -> new RuntimeException("Customer not found"));
		CustomerResponse customerResponse = conversionService.convert(customer, CustomerResponse.class);
		return ResponseEntity.ok(customerResponse);
	}

	@Override
	public ResponseEntity<CustomerResponse> createUser(CustomerRequest customerRequest) {
		Customer customer = conversionService.convert(customerRequest, Customer.class);
		Customer savedCustomer = customerService.save(customer);
		CustomerResponse customerResponse = conversionService.convert(savedCustomer, CustomerResponse.class);
		return ResponseEntity.ok(customerResponse);
	}

	@Override
	public ResponseEntity<Void> deleteUser(String dni) {
		customerService.delete(dni);
		return ResponseEntity.ok(null);
	}

	@Override
	public ResponseEntity<CustomerResponse> updateUser(Integer id, CustomerRequest customerRequest) {
		if (id == null) {
			throw new IllegalArgumentException("The given id must not be null");
		}
		Customer customer = conversionService.convert(customerRequest, Customer.class);
		customer.setId(id.longValue()); // Ensure the ID is set on the customer entity

		// Ensure the role is not null
		if (customer.getRole() == null) {
			throw new IllegalArgumentException("The role must not be null");
		}

		Customer updatedCustomer = customerService.update(customer);
		CustomerResponse customerResponse = conversionService.convert(updatedCustomer, CustomerResponse.class);
		return ResponseEntity.ok(customerResponse);
	}

}
