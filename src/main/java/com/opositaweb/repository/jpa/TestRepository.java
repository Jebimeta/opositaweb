package com.opositaweb.repository.jpa;

import com.opositaweb.repository.entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

	Optional<Object> findByName(String name);

}
