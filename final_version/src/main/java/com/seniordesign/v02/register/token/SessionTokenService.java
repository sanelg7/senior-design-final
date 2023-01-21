package com.seniordesign.v02.register.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Setter
@Getter
public class SessionTokenService {

    private final SessionTokenRepository sessionTokenRepository;

    public void saveSessionToken(SessionToken sessionToken){
        sessionTokenRepository.save(sessionToken);
    }
    public void updateSessionToken(SessionToken sessionToken,SessionToken oldSessiontoken){
        sessionTokenRepository.findSessionTokenByUser_Email(oldSessiontoken.getUser().getEmail()).setToken(sessionToken.getToken());
        sessionTokenRepository.save(oldSessiontoken);

    }

    public Optional<SessionToken> getToken(String token) {
        return sessionTokenRepository.findByToken(token);
    }

    public String getSessionToken(String email){
        SessionToken sessionToken = sessionTokenRepository.findSessionTokenByUser_Email(email);
        String token = sessionToken.getToken();
        return token;
    }



}
