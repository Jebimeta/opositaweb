package com.opositaweb.service.pdf.impl;

import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
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
	public Pdf findById(Long id) {
		Optional<Pdf> pdfUrl = pdfUrlRepository.findById(id);
		return pdfUrl.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_PDF_NOT_FOUND));
	}

	@Transactional
	@Override
	public Pdf findByName(String name) {
		Optional<Pdf> pdfUrl = pdfUrlRepository.findByName(name);
		return pdfUrl.orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_PDF_NOT_FOUND));
	}

	@Transactional
	@Override
	public Pdf save(Pdf pdf) {
		try {
			return pdfUrlRepository.save(pdf);
		}
		catch (Exception e) {
			throw new BusinessException(AppErrorCode.ERROR_SAVE);
		}
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
			throw new BusinessException(AppErrorCode.ERROR_UPDATE);
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
			throw new BusinessException(AppErrorCode.ERROR_DELETE);
		}
	}

}
