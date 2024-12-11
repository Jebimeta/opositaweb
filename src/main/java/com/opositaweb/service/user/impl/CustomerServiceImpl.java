package com.opositaweb.service.user.impl;

import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
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
		List<Customer> customers = userRepository.findAll().
				stream().
				filter(customer -> customer.getRole().equals(Rol.USER)).
				toList();
		if (customers.isEmpty()) {
			throw new BusinessException(AppErrorCode.ERROR_CUSTOMER_NOT_FOUND);
		}
		return customers;
	}

	@Transactional
	@Override
	public Customer findById(Long id) {
		Optional <Customer> user = userRepository.findById(id);
		return user.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_CUSTOMER_NOT_FOUND));
	}

	@Transactional
	@Override
	public Customer findByNameAndLastNames(String name, String lastNames) {
		Optional<Customer> user = userRepository.findByNameAndLastNames(name, lastNames);
		return user.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_CUSTOMER_NOT_FOUND));
	}

	@Transactional
	@Override
	public Customer findByDni(String dni) {
		Optional<Customer> user = userRepository.findByDni(dni);
		return user.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_CUSTOMER_NOT_FOUND));
	}

	@Transactional
	@Override
	public Customer findByEmail(String email) {
		Optional<Customer> user = userRepository.findByEmail(email);
		return user.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_CUSTOMER_NOT_FOUND));
	}

	@Transactional
	@Override
	public Customer save(Customer customer) {
		try{
			customer.setRole(Rol.USER);
			customer.setStatus(false);
			customer.setSubscriptionStatus(false);
			return userRepository.save(customer);
		} catch (Exception e) {
			throw new BusinessException(AppErrorCode.ERROR_SAVE, e);
		}
	}

	@Transactional
	@Override
	public Customer update(Customer customer) {
		Optional<Customer> existingUser = userRepository.findById(customer.getId());
		if (existingUser.isPresent()) {
			Customer updatedCustomer = existingUser.get();
			updatedCustomer.setName(customer.getName());
			updatedCustomer.setLastNames(customer.getLastNames());
			updatedCustomer.setDni(customer.getDni());
			updatedCustomer.setEmail(customer.getEmail());
			updatedCustomer.setTelephone(customer.getTelephone());
			return userRepository.save(updatedCustomer);
		} else {
			throw new BusinessException(AppErrorCode.ERROR_UPDATE);
		}
	}

	@Transactional
	@Override
	public void delete(String dni) {
		Optional<Customer> user = userRepository.findByDni(dni);
		if (user.isPresent()) {
			userRepository.delete(user.get());
		} else {
			throw new BusinessException(AppErrorCode.ERROR_DELETE);
		}
	}

}
