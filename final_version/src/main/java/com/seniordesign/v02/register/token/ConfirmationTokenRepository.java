package com.seniordesign.v02.register.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);
    List<ConfirmationToken> findConfirmationTokenByUser_Email(String email);


    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " + "SET c.verificationTime = ?2 " + "WHERE c.token = ?1")
    int updateConfirmationTime(String token, LocalDateTime verificationTime);
}
