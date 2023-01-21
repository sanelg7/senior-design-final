package com.seniordesign.v02.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User,Long> {

    //SELECT * from application_users where email = email;
    //@Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User user " + "SET user.enabled = TRUE WHERE user.email = ?1")
    int enableUser(String email);
}
