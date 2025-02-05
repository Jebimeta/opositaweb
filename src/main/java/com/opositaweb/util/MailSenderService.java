package com.opositaweb.util;

import com.opositaweb.config.properties.OpositaWebProperties;
import com.opositaweb.domain.vo.EmailResponse;
import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import com.opositaweb.service.about.EmailResponseFactory;
import com.opositaweb.service.user.CustomerService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;
    private final OpositaWebProperties properties;
    private final CustomerService customerService;

    public EmailResponse receiveContactUs(String senderName, String phoneNumber, String emailMessage)
        throws BusinessException {

        String subject = "Contacta con nosotros";
        String email = properties.getMail().getHostEmail();
        String headerMessage = "Se ha recibido un correo a través del formulario de contacta con nosotros, "
                 + "los datos del remitente son los siguientes: ";
        String content = headerMessage + "\n"
                + "Nombre: " + senderName + "\n"
                + "Teléfono: " + phoneNumber + "\n"
                + "Correo: " + email + "\n"
                + "Mensaje: " + emailMessage;
        try{
            sendMessage(email, senderName, subject, content);
            return EmailResponseFactory.createEmailResponse("Correo enviado correctamente");
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BusinessException(AppErrorCode.ERROR_EMAIL);
        }
    }

    private void sendMessage(String email, String senderName, String subject, String content)
        throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(properties.getMail().getUsername(), senderName);
        helper.setFrom(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

}
