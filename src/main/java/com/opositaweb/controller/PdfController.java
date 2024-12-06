package com.opositaweb.controller;

import com.opositaweb.apifirst.api.PdfApiDelegate;
import com.opositaweb.domain.vo.PdfRequest;
import com.opositaweb.domain.vo.PdfResponse;
import com.opositaweb.repository.entities.Pdf;
import com.opositaweb.service.pdf.PdfUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PdfController implements PdfApiDelegate {

	private final PdfUrlService pdfUrlService;

	private final ConversionService conversionService;

	@Override
	public ResponseEntity<PdfResponse> getPdfUrlById(Integer id) {
		Pdf pdf = pdfUrlService.findById(id.longValue()).orElseThrow(() -> new RuntimeException("Pdf not found"));
		PdfResponse pdfResponse = conversionService.convert(pdf, PdfResponse.class);
		return ResponseEntity.ok(pdfResponse);
	}

	@Override
	public ResponseEntity<List<PdfResponse>> getPdfUrls() {
		List<PdfResponse> pdfs = pdfUrlService.findAll()
			.stream()
			.map(pdf -> conversionService.convert(pdf, PdfResponse.class))
			.toList();
		return ResponseEntity.ok(pdfs);
	}

	@Override
	public ResponseEntity<PdfResponse> createPdfUrl(PdfRequest pdfRequest) {
		Pdf pdf = conversionService.convert(pdfRequest, Pdf.class);
		Pdf savedPdf = pdfUrlService.save(pdf);
		PdfResponse pdfResponse = conversionService.convert(savedPdf, PdfResponse.class);
		return ResponseEntity.ok(pdfResponse);
	}

	@Override
	public ResponseEntity<Void> deletePdfUrl(Integer id) {
		pdfUrlService.delete(id.longValue());
		return ResponseEntity.ok(null);
	}

	@Override
	public ResponseEntity<PdfResponse> updatePdfUrl(Integer id, PdfRequest pdfRequest) {
		Pdf pdf = conversionService.convert(pdfRequest, Pdf.class);
		Pdf updatedPdf = pdfUrlService.update(pdf);
		PdfResponse pdfResponse = conversionService.convert(updatedPdf, PdfResponse.class);
		return ResponseEntity.ok(pdfResponse);
	}

}
