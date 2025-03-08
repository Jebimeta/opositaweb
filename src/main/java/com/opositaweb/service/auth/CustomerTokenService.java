package com.opositaweb.service.auth;

import com.opositaweb.domain.vo.AuthenticationResponse;
import com.opositaweb.repository.entities.Customer;
import com.opositaweb.repository.entities.Token;
import com.opositaweb.repository.jpa.TokenJpaRepository;
import com.opositaweb.repository.jpa.UserRepository;
import com.opositaweb.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerTokenService {

    private final TokenJpaRepository tokenJpaRepository;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    // Revoca todos los tokens de un usuario
    public void revokeAllTokenByUser(Customer customer) {
        List<Token> validTokens = tokenJpaRepository.findByAllAccessTokenByUser(customer.getId());

        if(validTokens.isEmpty()){
            return;
        }

        validTokens.forEach((token -> token.setLoggedOut(true)));
        tokenJpaRepository.saveAll(validTokens);
    }

    // Guarda el token de acceso y el token de refresco en la base de datos
    public void saveUserToken(String accessToken, String refreshToken, Customer customer) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setCustomer(customer);
        tokenJpaRepository.save(token);
    }

    // Busca un usuario por su token de refresco
    public Customer findUserByRefreshToken(String refreshToken){
        String username = jwtService.extractUsername(refreshToken);
        return userRepository.findByNameAndLastNames(username, username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Crea nuevos tokens de acceso y refresco
    public AuthenticationResponse createNewTokens(Customer customer){
        String newAccessToken = jwtService.generateAccessToken(customer);
        String newRefreshToken = jwtService.generateRefreshToken(customer);

        revokeAllTokenByUser(customer);
        saveUserToken(newAccessToken, newRefreshToken, customer);

        return new AuthenticationResponse(newAccessToken, newRefreshToken, "New token generated");
    }

    // Valida y genera nuevos tokens de acceso y refresco
    public ResponseEntity<AuthenticationResponse> validateAndGenerateNewTokens(String refreshToken, Customer customer){
        if(!jwtService.isValidRefreshToken(refreshToken, customer)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        AuthenticationResponse response = createNewTokens(customer);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Refresca los tokens de acceso y refresco
    public ResponseEntity<AuthenticationResponse> refreshTokens(HttpServletRequest request){
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = authHeader.substring(7);

        if(authHeader.startsWith("Bearer ")){
            Customer customer = findUserByRefreshToken(refreshToken);
            return validateAndGenerateNewTokens(refreshToken, customer);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
