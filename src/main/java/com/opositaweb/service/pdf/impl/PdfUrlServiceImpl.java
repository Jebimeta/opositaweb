package com.opositaweb.service.pdf.impl;

import com.opositaweb.repository.entities.Pdf;
import com.opositaweb.repository.jpa.PdfUrlRepository;
import com.opositaweb.service.pdf.PdfUrlService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PdfUrlServiceImpl implements PdfUrlService {

	private final PdfUrlRepository pdfUrlRepository;

	@Transactional
	@Override
	public List<Pdf> findAll() {
		return pdfUrlRepository.findAll();
	}

	@Transactional
	@Override
	public Optional<Pdf> findById(Long id) {
		Optional<Pdf> pdfUrl = pdfUrlRepository.findById(id);
		if (pdfUrl.isPresent()) {
			return pdfUrl;
		}
		else {
			throw new RuntimeException("PdfUrl not found");
		}
	}

	@Transactional
	@Override
	public Optional<Pdf> findByName(String name) {
		Optional<Pdf> pdfUrl = pdfUrlRepository.findByName(name);
		if (pdfUrl.isPresent()) {
			return pdfUrl;
		}
		else {
			throw new RuntimeException("PdfUrl not found");
		}
	}

	@Transactional
	@Override
	public Pdf save(Pdf pdf) {
		return pdfUrlRepository.save(pdf);
	}

	@Transactional
	@Override
	public Pdf update(Pdf pdf) {
		Optional<Pdf> pdfUrlOptional = pdfUrlRepository.findById(pdf.getId());
		if (pdfUrlOptional.isPresent()) {
			Pdf pdfToUpdate = pdfUrlOptional.get();
			pdfToUpdate.setName(pdf.getName());
			pdfToUpdate.setUrl(pdf.getUrl());
			return pdfUrlRepository.save(pdfToUpdate);
		}
		else {
			throw new RuntimeException("PdfUrl not found");
		}
	}

	@Transactional
	@Override
	public void delete(Long id) {
		Optional<Pdf> pdfUrl = pdfUrlRepository.findById(id);
		if (pdfUrl.isPresent()) {
			pdfUrlRepository.deleteById(id);
		}
		else {
			throw new RuntimeException("PdfUrl not found");
		}
	}

}
