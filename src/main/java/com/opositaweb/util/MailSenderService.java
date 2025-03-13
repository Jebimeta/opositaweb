package com.opositaweb.util;

import com.opositaweb.config.properties.OpositaWebProperties;
import com.opositaweb.domain.vo.EmailResponse;
import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import com.opositaweb.repository.entities.Customer;
import com.opositaweb.service.about.EmailResponseFactory;
import com.opositaweb.service.user.CustomerService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailSenderService {

    private final JavaMailSender mailSender;

    private final OpositaWebProperties opositaWebProperties;

    private final CustomerService customerService;

    private final CustomerService tokenService;

    // Envia un correo electrónico al administrador con los datos y el mensaje del remitente.
    public EmailResponse receiveContactUs(String senderName, String phoneNumber, String emailMessage)
            throws BusinessException {

        String subject = "Contacta con nosotros";
        String email = opositaWebProperties.getMail().getHostEmail();
        String headerMessage = "Se ha recibido un correo a través del formulario de contacta con nosotros, "
                + "los datos del remitente son los siguientes: <br>";
        String content = headerMessage + "<br>"
                + "Nombre: " + senderName + "<br>"
                + "Teléfono: " + phoneNumber + "<br>"
                + "Correo: " + email + "<br><br>"
                + "Mensaje: " + emailMessage;
        try {
            sendMessage(email, senderName, subject, content);
            return EmailResponseFactory.createEmailResponse("Correo enviado correctamente");
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BusinessException(AppErrorCode.ERROR_EMAIL);
        }
    }

    // Envia el correo electrónico
    private void sendMessage(String email, String senderName, String subject, String content)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(opositaWebProperties.getMail().getUsername(), senderName);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    // Envia un correo de verificación al usuario
    public void sendVerificationEmail(Customer user) throws BusinessException {
        String subject = "Verificación de Registro";
        String senderName = "Oposita Web";
        String verifyURL = opositaWebProperties.getMail().getHost() + "/auth/verify/" + user.getVerificationToken();

        String content = "Estimado " + user.getName() + " " + user.getLastNames() + ",<br>"
                + "Por favor, haga clic en el siguiente enlace para verificar su registro:<br>" + "<h3><a href=\""
                + verifyURL + "\" target=\"_self\">VERIFICAR</a></h3>" + "Gracias,<br>" + senderName + ".";

        try {
            sendMessage(user.getEmail(), senderName, subject, content);
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            log.error(e);
            throw new BusinessException(AppErrorCode.ERROR_EMAIL);
        }

    }

    // Envia un correo de recuperación de contraseña
    public EmailResponse sendPasswordRecoveryEmail(String email) {
        String subject = "Restablecer contraseña";
        String senderName = "OpositaWeb";

        Customer customer = customerService.findByEmail(email);

        String verificationToken = tokenService.generateVerificationToken(customer);
        customer.setVerificationToken(verificationToken);

        String verifyURL = opositaWebProperties.getMail().getHost() + "/auth/password-reset/request/"
                + customer.getVerificationToken();

        String content = "Estimado " + customer.getName() + ",<br>"
                + "Por favor, haga clic en el siguiente enlace para asignar una nueva contraseña:<br>"
                + "<h3><a href=\"" + verifyURL + "\" target=\"_self\">CAMBIAR CONTRASEÑA</a></h3>" + "Gracias,<br>"
                + senderName + ".";

        try {
            sendMessage(customer.getEmail(), senderName, subject, content);
            customerService.update(customer);
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            log.error(e);
            throw new BusinessException(AppErrorCode.ERROR_EMAIL);
        }
        return new EmailResponse("Se ha creado el correo correctamente.");
    }

}