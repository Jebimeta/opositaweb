package com.opositaweb.controller;

import com.opositaweb.apifirst.api.AuthApiDelegate;
import com.opositaweb.domain.vo.*;
import com.opositaweb.repository.entities.Customer;
import com.opositaweb.service.auth.AuthenticationService;
import com.opositaweb.service.auth.CustomerTokenService;
import com.opositaweb.service.auth.PasswordRecoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApiDelegate {

    private final AuthenticationService authenticationService;

    private final CustomerTokenService customerTokenService;

    private final ConversionService conversionService;

    private final PasswordRecoveryService passwordRecoveryService;

    @Override
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody CustomerRequest customerRequest){
        Customer customer = conversionService.convert(customerRequest, Customer.class);
        return ResponseEntity.ok(authenticationService.register(customer));
    }

    @Override
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody LoginRequest loginRequest){
        Customer customer = conversionService.convert(loginRequest, Customer.class);
        return ResponseEntity.ok(authenticationService.authenticate(customer));
    }

    @Override
    public ResponseEntity<String> verifyUser(@PathVariable("token") String token){
        return authenticationService.verifyUser(token);
    }

    @Override
    public ResponseEntity<CustomerResponse> getAuthenticatedUser(){
        CustomerResponse authenticatedCustomer = authenticationService.getAuthenticatedUser();
        return ResponseEntity.ok(authenticatedCustomer);
    }

    @Tag(name = "auth")
    @PostMapping(value = "/api/v1/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Refreshes the authentication token using the regresh token.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Token refreshed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Authentication failed") })
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request){
        return customerTokenService.refreshTokens(request);
    }

    @Override
    public ResponseEntity<CustomerResponse> passwordResetConfirm(PasswordResetConfirmRequest passwordResetConfirmRequest){
        Customer customer = passwordRecoveryService.confirmRecoveryPassword(passwordResetConfirmRequest);
        CustomerResponse customerResponse = conversionService.convert(customer, CustomerResponse.class);
        return ResponseEntity.ok(customerResponse);
    }

    @Override
    public ResponseEntity<EmailResponse> passwordReset(PasswordResetRequest passwordResetRequest){
        EmailResponse emailResponse = passwordRecoveryService.sendEmailToRecoveryPassword(passwordResetRequest);
        return ResponseEntity.ok(emailResponse);
    }
}

