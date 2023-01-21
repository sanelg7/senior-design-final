package com.seniordesign.v02.passwordCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordCategoryRepository extends JpaRepository<PasswordCategory,Long> {

    List<PasswordCategory> findPasswordByUser_Email(String email);

    PasswordCategory findPasswordCategoryByPasswordCategoryID(Long passwordCategoryID);
    PasswordCategory findPasswordCategoryByPasswords_PasswordID(Long passwordId);
}
