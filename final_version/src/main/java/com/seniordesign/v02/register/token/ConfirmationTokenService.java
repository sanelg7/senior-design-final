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
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }


    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmationTime(String token) {
        return confirmationTokenRepository.updateConfirmationTime(token, LocalDateTime.now());
    }

   public boolean checkConfirmationTokens(String email){
        List<ConfirmationToken> confirmationTokens = confirmationTokenRepository.findConfirmationTokenByUser_Email(email);
        List<LocalDateTime> verificationTimes = confirmationTokens.stream().map(ConfirmationToken::getVerificationTime).collect(Collectors.toList());

       boolean isConfirmedAlready = false;
        for(LocalDateTime l : verificationTimes){
            if(l != null){
                isConfirmedAlready = true;
            }
        }
        return isConfirmedAlready;
    }
}
