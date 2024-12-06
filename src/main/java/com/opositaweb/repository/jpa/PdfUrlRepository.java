package com.opositaweb.repository.jpa;

import com.opositaweb.repository.entities.Pdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PdfUrlRepository extends JpaRepository<Pdf, Long> {

	Optional<Pdf> findByName(String name);

}
