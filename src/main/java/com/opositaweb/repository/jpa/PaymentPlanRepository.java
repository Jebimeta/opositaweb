package com.opositaweb.repository.jpa;

import com.opositaweb.repository.entities.PaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {

	Optional<PaymentPlan> findByName(String name);

}
