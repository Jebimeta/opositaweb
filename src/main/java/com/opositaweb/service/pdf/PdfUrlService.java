package com.opositaweb.service.pdf;

import com.opositaweb.repository.entities.Pdf;

import java.util.List;
import java.util.Optional;

public interface PdfUrlService {

	List<Pdf> findAll();

	Pdf findById(Long id);

	Pdf findByName(String name);

	Pdf save(Pdf pdf);

	Pdf update(Pdf pdf);

	void delete(Long id);

}
