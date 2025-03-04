package com.opositaweb.service.user;

import com.opositaweb.repository.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

	List<Customer> findAll();

	Customer findById(Long id);

	Customer findByNameAndLastNames(String name, String lastNames);

	Customer findByDni(String dni);

	Customer findByEmail(String email);

	Customer save(Customer customer);

	Customer update(Customer customer);

	void delete(String dni);

	String generateVerificationToken(Customer customer);

	Customer verifyCustomerByToken(String token);

	Customer findCustomerByVerificationToken(String token);

	Customer getCustomerByUserName(String name);

}
