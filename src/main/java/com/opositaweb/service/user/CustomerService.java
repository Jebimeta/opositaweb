package com.opositaweb.service.user;

import com.opositaweb.repository.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

	List<Customer> findAll();

	Optional<Customer> findById(Long id);

	Optional<Customer> findByNameAndLastNames(String name, String lastNames);

	Optional<Customer> findByDni(String dni);

	Optional<Customer> findByEmail(String email);

	Customer save(Customer customer);

	Customer update(Customer customer);

	void delete(String dni);

}
