package com.opositaweb.repository.jpa;

import com.opositaweb.repository.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;


public interface TokenJpaRepository extends JpaRepository<Token, Long> {

	@Query("""
        SELECT t
        FROM Token t
        INNER JOIN Customer c ON t.customer.id = c.id
        WHERE t.customer.id = :customerId
        AND t.loggedOut = false
    """)

	List<Token> findByAllAccessTokenByUser(Token token);

	Optional<Token> findByAccessToken(String accessToken);

	Optional<Token> findByRefreshToken(String refreshToken);
}
