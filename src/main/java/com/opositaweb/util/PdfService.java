package com.opositaweb.util;

import com.opositaweb.config.properties.OpositaWebProperties;
import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class PdfService {

    private final OpositaWebProperties opositaWebProperties;

    // Guarda una imagen en un directorio específico del sistema de archivos
    public String uploadPdfDirectory(MultipartFile pdf) throws IOException{
        Path pdfPath = Paths.get(opositaWebProperties.getUpload().getDirectory());
        String pdfName = getValidPdfName(pdf);
        if (!Files.exists(pdfPath)) {
            Files.createDirectories(pdfPath);
        }
        savePdf(pdf, pdfPath.resolve(pdfName));
        return pdfName;
    }

    //Valida y obtiene el nombre del pdf
    private String getValidPdfName(MultipartFile pdf) {
        String pdfName = pdf.getOriginalFilename();
        if (StringUtils.isEmpty(pdfName)) {
            throw new BusinessException(AppErrorCode.ERROR_SAVE_PDF_NAME);
        }
        return pdfName;
    }

    // Guarda el pdf en el directorio
    private void savePdf(MultipartFile pdf, Path destination) throws IOException {
        try(InputStream inputStream = pdf.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new BusinessException(AppErrorCode.ERROR_SAVE_PDF, e);
        }
    }

    // Elimina un pdf del directorio
    public void deletePdf(String pdfName) {
        Path pdfPath = Paths.get(opositaWebProperties.getUpload().getDirectory(), pdfName);
        try {
            Files.deleteIfExists(pdfPath);
        } catch (IOException e) {
            throw new BusinessException(AppErrorCode.ERROR_DELETE_PDF, e);
        }
    }
}
