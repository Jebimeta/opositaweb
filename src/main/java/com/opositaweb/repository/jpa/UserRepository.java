package com.opositaweb.repository.jpa;

import com.opositaweb.repository.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByNameAndLastNames(String name, String lastNames);

	Optional<Customer> findByDni(String dni);

	Optional<Customer> findByEmail(String email);

	Optional<Customer> findByVerificationToken(String token);

}
