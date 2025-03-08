package com.opositaweb.service.auth.impl;

import com.opositaweb.domain.vo.EmailResponse;
import com.opositaweb.domain.vo.PasswordResetConfirmRequest;
import com.opositaweb.domain.vo.PasswordResetRequest;
import com.opositaweb.repository.entities.Customer;
import com.opositaweb.service.auth.PasswordRecoveryService;
import com.opositaweb.service.user.CustomerService;
import com.opositaweb.util.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    private final CustomerService updateService;

    private final MailSenderService mailSenderService;

    private final CustomerService verificationTokenService;

    // Envía un correo electrónico al usuario con un enlace para restablecer la contraseña.
    @Override
    public EmailResponse sendEmailToRecoveryPassword(PasswordResetRequest passwordResetRequest) {
        return mailSenderService.sendPasswordRecoveryEmail(passwordResetRequest.getEmail());
    }

    // Confirma el restablecimiento de la contraseña y actualiza la contraseña del usuario.
    @Override
    public Customer confirmRecoveryPassword(PasswordResetConfirmRequest passwordResetConfirmRequest) {
        Customer customer = verificationTokenService.findCustomerByVerificationToken(passwordResetConfirmRequest.getVerificationToken());
        customer.setPassword(passwordResetConfirmRequest.getNewPassword());
        updateService.update(customer);

        return customer;
    }
}
