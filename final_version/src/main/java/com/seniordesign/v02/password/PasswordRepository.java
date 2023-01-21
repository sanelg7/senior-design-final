package com.seniordesign.v02.password;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password,Long> {
    Password findPasswordByPasswordID(Long passwordId);
}
