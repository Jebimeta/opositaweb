package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.PdfResponse;
import com.opositaweb.repository.entities.Pdf;
import org.springframework.core.convert.converter.Converter;

public interface PdfToPdfResponseMapper extends Converter<Pdf, PdfResponse> {

	PdfResponse convert(Pdf source);

}
