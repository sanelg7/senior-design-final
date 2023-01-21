package com.seniordesign.v02.user;

import com.seniordesign.v02.register.token.ConfirmationToken;
import com.seniordesign.v02.register.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException("User with id " + userId + "does not exist!");
        }
        return userRepository.findById(userId).get();
    }




    public void addUser(User user) {
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());
        if(userByEmail.isPresent()){
            throw new IllegalStateException("User with email already signed up");
        }else{
            userRepository.save(user);
        }
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException("User with id " + userId + "does not exist!");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId,
                           String name,
                           String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "User with id " + userId + "does not exist, can not update!"));
        if(name != null &&
                name.length() > 0 &&
                !Objects.equals(user.getName(),name)){
            user.setName(name);
        }

        if(email != null &&
                email.length() > 0 &&
                !Objects.equals(user.getEmail(),email)){
            Optional<User> userOptional = userRepository
                    .findUserByEmail(email);
            if(userOptional.isPresent()){
                throw new IllegalStateException("email is used by another user!");
            }
            user.setEmail(email);
        }

    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No email found!"));
    }

    public String signUserUp(User user){

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(60),user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }


    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

}
