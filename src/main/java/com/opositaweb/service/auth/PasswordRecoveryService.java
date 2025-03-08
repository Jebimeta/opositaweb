package com.opositaweb.service.auth;

import com.opositaweb.domain.vo.EmailResponse;
import com.opositaweb.domain.vo.PasswordResetConfirmRequest;
import com.opositaweb.domain.vo.PasswordResetRequest;
import com.opositaweb.repository.entities.Customer;

public interface PasswordRecoveryService {

    public EmailResponse sendEmailToRecoveryPassword(PasswordResetRequest passwordResetRequest);

    public Customer confirmRecoveryPassword(PasswordResetConfirmRequest passwordResetConfirmRequest);
}
