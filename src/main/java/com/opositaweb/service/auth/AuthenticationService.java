package com.opositaweb.service.auth;

import com.opositaweb.domain.vo.AuthenticationResponse;
import com.opositaweb.domain.vo.CustomerResponse;
import com.opositaweb.domain.vo.RegisterResponse;
import com.opositaweb.exception.AppErrorCode;
import com.opositaweb.exception.BusinessException;
import com.opositaweb.repository.entities.Customer;
import com.opositaweb.repository.jpa.UserRepository;
import com.opositaweb.service.user.CustomerService;
import com.opositaweb.service.user.factory.CustomerRequestFactory;
import com.opositaweb.util.JwtService;
import com.opositaweb.util.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final CustomerService customerService;

    private final MailSenderService mailSenderService;

    private final CustomerTokenService customerTokenService;

    private final CustomerRequestFactory customerRequestFactory;

    private final ConversionService conversionService;

    private final JwtService jwtService;

    // Autentica un usuario y genera tokens de acceso y refresco
    public AuthenticationResponse authenticate(Customer loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        Customer customer = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        if(Boolean.FALSE.equals(customer.isStatus())){
            throw new BusinessException(AppErrorCode.ERROR_NOT_VERIFIED_ACCOUNT);
        }

        String accessToken = jwtService.generateAccessToken(customer);
        String refreshToken = jwtService.generateRefreshToken(customer);

        customerTokenService.revokeAllTokenByUser(customer);
        customerTokenService.saveUserToken(accessToken, refreshToken, customer);

        return new AuthenticationResponse(accessToken, refreshToken, "Login successful");
    }

    // Registra un nuevo usuario y envía un correo de verificación
    public RegisterResponse register(Customer request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return new RegisterResponse(null, null, "User already exists");
        }

        Customer customer = customerRequestFactory.createCustomerRequest(request);
        String verificationToken = customerService.generateVerificationToken(customer);
        customer.setVerificationToken(verificationToken);
        userRepository.save(customer);
        return sendVerificationEmail(customer);
    }

    // Envía un correo de verificación al usuario
    private RegisterResponse sendVerificationEmail(Customer customer){
        try{
            userRepository.save(customer);
            mailSenderService.sendVerificationEmail(customer);
        } catch (BusinessException e) {
            userRepository.delete(customer);
            return new RegisterResponse(null, null, "Failed to send verification email. Please try again");
        }

        return new RegisterResponse(null, null, "User registered susccessfully. Please check your email to verify your account");
    }

    // Verifica un usuario por su token de verificación
    public ResponseEntity<String> verifyUser(String token){
        try{
            Customer customer = customerService.verifyCustomerByToken(token);
            customer.setStatus(true);
            customer.setVerificationToken(null);
            userRepository.save(customer);
            return new ResponseEntity<>("Account verifiedSuccessfully", HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>("Invalid verification token", HttpStatus.BAD_REQUEST);
        }
    }

    // Obtiene el usuario autenticado
    public CustomerResponse getAuthenticatedUser(){
        String name = getNameFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Customer findedCustomer = userRepository.findByNameAndLastNames(name, name).orElseThrow(() -> new BusinessException(AppErrorCode.ERROR_CUSTOMER_NOT_FOUND));
        return conversionService.convert(findedCustomer, CustomerResponse.class);
    }

    // Obtiene el nombre de usuario del principal
    private String getNameFromPrincipal(Object principal){
        return(principal instanceof UserDetails userDetails) ? userDetails.getUsername(): throwUserNotAuthenticatedException();
    }

    // Lanza una excepción si el usuario no está autenticado
    private String throwUserNotAuthenticatedException(){
        throw new BusinessException(AppErrorCode.ERROR_NOT_AUTHENTICATED_ACCOUNT);
    }

    // Obtiene un usuario por su token de acceso
    public Customer findUserByTokenAccess(){
        CustomerResponse customerResponse = getAuthenticatedUser();
        return customerService.getCustomerByUserName(customerResponse.getName());
    }
}
