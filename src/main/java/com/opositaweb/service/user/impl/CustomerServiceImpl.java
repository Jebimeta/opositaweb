package com.opositaweb.service.user.impl;

import com.opositaweb.repository.entities.Customer;
import com.opositaweb.repository.enums.Rol;
import com.opositaweb.repository.jpa.UserRepository;
import com.opositaweb.service.user.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final UserRepository userRepository;

	@Transactional
	@Override
	public List<Customer> findAll() {
		List<Customer> customers = userRepository.findAll();
		return customers;
	}

	@Transactional
	@Override
	public Optional<Customer> findById(Long id) {
		Optional<Customer> user = userRepository.findById(id);
		if (user.isPresent()) {
			return user;
		}
		else {
			throw new RuntimeException("User not found");
		}
	}

	@Transactional
	@Override
	public Optional<Customer> findByNameAndLastNames(String name, String lastNames) {
		Optional<Customer> user = userRepository.findByNameAndLastNames(name, lastNames);
		if (user.isPresent()) {
			return user;
		}
		else {
			throw new RuntimeException("User not found");
		}
	}

	@Transactional
	@Override
	public Optional<Customer> findByDni(String dni) {
		Optional<Customer> user = userRepository.findByDni(dni);
		if (user.isPresent()) {
			return user;
		}
		else {
			throw new RuntimeException("User not found");
		}
	}

	@Transactional
	@Override
	public Optional<Customer> findByEmail(String email) {
		Optional<Customer> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			return user;
		}
		else {
			throw new RuntimeException("User not found");
		}
	}

	@Transactional
	@Override
	public Customer save(Customer customer) {
		customer.setRole(Rol.USER);
		customer.setStatus(false);
		return userRepository.save(customer);
	}

	@Transactional
	@Override
	public Customer update(Customer customer) {
		Optional<Customer> existingUser = userRepository.findById(customer.getId());
		if (existingUser.isPresent()) {
			Customer updatedCustomer = existingUser.get();
			updatedCustomer.setName(customer.getName());
			updatedCustomer.setLastNames(customer.getLastNames());
			updatedCustomer.setEmail(customer.getEmail());
			updatedCustomer.setTelephone(customer.getTelephone());
			updatedCustomer.setRole(customer.getRole());
			return userRepository.save(updatedCustomer);
		}
		else {
			throw new RuntimeException("User not found");
		}
	}

	@Transactional
	@Override
	public void delete(String dni) {
		Optional<Customer> user = userRepository.findByDni(dni);
		if (user.isPresent()) {
			userRepository.delete(user.get());
		}
		else {
			throw new RuntimeException("User not found");
		}
	}

}
