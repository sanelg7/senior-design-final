package com.seniordesign.v02.register.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionTokenRepository extends JpaRepository<SessionToken,Long > {

    Optional<SessionToken> findByToken(String token);
    SessionToken findSessionTokenByUser_Email(String email);


}
