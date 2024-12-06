package com.opositaweb.controller.mapper;

import com.opositaweb.domain.vo.PdfRequest;
import com.opositaweb.repository.entities.Pdf;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public interface PdfRequestToPdfMapper extends Converter<PdfRequest, Pdf> {

	Pdf convert(@NonNull PdfRequest source);

}
