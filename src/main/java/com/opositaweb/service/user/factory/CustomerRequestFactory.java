package com.opositaweb.service.user.factory;

import com.opositaweb.repository.entities.Customer;
import com.opositaweb.repository.enums.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRequestFactory {

    private final PasswordEncoder passwordEncoder;

    // Crea un nuevo usuario a partir de una petición de registro.
    public Customer createCustomerRequest(Customer request){
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setLastNames(request.getLastNames());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setDni(request.getDni());
        customer.setTelephone(request.getTelephone());
        customer.setRole(Rol.USER);
        customer.setStatus(false);
        return customer;
    }
}
