package com.opositaweb.service.about;

import com.opositaweb.domain.vo.EmailRequest;
import com.opositaweb.domain.vo.EmailResponse;
import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import com.opositaweb.util.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//Es un servicio de Spring Boot que maneja el envío de correos a traves de MailSenderService
@Service
@RequiredArgsConstructor
@Slf4j
public class AboutService {

    private final MailSenderService mailSenderService;

    //Recibe los datos del formulario de contacto y los envía a MailSenderService
    public EmailResponse sendEmail(EmailRequest emailRequest) {
        try {
            return mailSenderService.receiveContactUs(emailRequest.getSenderName(), emailRequest.getPhoneNumber(), emailRequest.getEmailMessage());
        } catch (BusinessException exception) {
            log.error(String.valueOf(AppErrorCode.ERROR_EMAIL), exception.toString());
            return EmailResponseFactory.createEmailResponse(AppErrorCode.ERROR_EMAIL.getMessage());
        }

    }
}
