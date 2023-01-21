package com.seniordesign.v02.register.login;

import com.seniordesign.v02.register.token.SessionToken;
import com.seniordesign.v02.register.token.SessionTokenRepository;
import com.seniordesign.v02.register.token.SessionTokenService;
import com.seniordesign.v02.user.User;
import com.seniordesign.v02.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Setter
@Getter
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SessionTokenService sessionTokenService;
    private final SessionTokenRepository sessionTokenRepository;

    public String login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findUserByEmail(loginRequest.getEmail());
        boolean exists = userOptional.isPresent();
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Wrong credentials")
                    .toString();
            return json;
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Wrong credentials pw")
                    .toString();
            return json;
        }
        if(user.getEnabled()==false){
            String json = new JSONObject()
                    .put("status", "success")
                    .put("token", user.getSessionToken())
                    .put("email", user.getEmail())
                    .put("verifiedByEmail", user.getEnabled())
                    .put("name", user.getName())
                    .put("surname", user.getLastName())
                    .put("secondName", user.getSecondName())
                    .toString();
            return json;
        }else{
            if(sessionTokenRepository.findSessionTokenByUser_Email(user.getEmail())==null){
                String token = UUID.randomUUID().toString();
                SessionToken sessionToken = new SessionToken(token,user);
                sessionTokenService.saveSessionToken(sessionToken);

                String json = new JSONObject()
                        .put("status", "success")
                        .put("token", sessionToken.getToken())
                        .put("email", user.getEmail())
                        .put("verifiedByEmail", user.getEnabled())
                        .put("name", user.getName())
                        .put("surname", user.getLastName())
                        .put("secondName", user.getSecondName())
                        .toString();
                return json;



        }if(sessionTokenRepository.findSessionTokenByUser_Email(user.getEmail()).getToken()==null){
                String token = UUID.randomUUID().toString();
                SessionToken sessionToken = sessionTokenRepository.findSessionTokenByUser_Email(user.getEmail());
                sessionToken.setToken(token);
                sessionTokenRepository.save(sessionToken);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("token", sessionToken.getToken())
                        .put("email", user.getEmail())
                        .put("verifiedByEmail", user.getEnabled())
                        .put("name", user.getName())
                        .put("surname", user.getLastName())
                        .put("secondName", user.getSecondName())
                        .toString();
                return json;

            }if(sessionTokenRepository.findSessionTokenByUser_Email(user.getEmail()).getToken()!=null){
                String token = UUID.randomUUID().toString();
                Long oldTokenID = sessionTokenRepository.findSessionTokenByUser_Email(user.getEmail()).getId();
                SessionToken oldSessionToken = sessionTokenRepository.getById(oldTokenID);
                SessionToken sessionToken = new SessionToken(token,user);
                sessionTokenService.updateSessionToken(sessionToken,oldSessionToken);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("token", sessionToken.getToken())
                        .put("email", user.getEmail())
                        .put("verifiedByEmail", user.getEnabled())
                        .put("name", user.getName())
                        .put("surname", user.getLastName())
                        .put("secondName", user.getSecondName())
                        .toString();
                return json;

            }

        }
        return "";


    }


    public String logout(LogoutRequest logoutRequest){

        Optional<User> userOptional = userRepository.findUserByEmail(logoutRequest.getEmail());
        String json = new JSONObject()
                .put("status", "success")
                .toString();
        SessionToken sessionToken = sessionTokenRepository.findSessionTokenByUser_Email(logoutRequest.getEmail());
        sessionToken.setToken(null);
                sessionTokenRepository.save(sessionToken);
        return json;

    }

}
