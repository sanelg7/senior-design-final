package com.seniordesign.v02.register;

import com.seniordesign.v02.email.EmailSender;
import com.seniordesign.v02.email.EmailTemplates;
import com.seniordesign.v02.register.token.ConfirmationToken;
import com.seniordesign.v02.register.token.ConfirmationTokenRepository;
import com.seniordesign.v02.register.token.ConfirmationTokenService;
import com.seniordesign.v02.register.token.SessionTokenRepository;
import com.seniordesign.v02.user.User;
import com.seniordesign.v02.user.UserRepository;
import com.seniordesign.v02.user.UserRole;
import com.seniordesign.v02.user.UserService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final SessionTokenRepository sessionTokenRepository;

    @PostMapping
    public String register(RegistrationRequest request) {
        boolean isValid = emailValidator.test(request.getEmail());
        boolean exists = userRepository.findUserByEmail(request.getEmail()).isPresent();
        if(!isValid){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Entered email is not valid.")
                    .toString();
            return json;
        }
        if(exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Email is already taken")
                    .toString();
            return json;

        }
        userService.signUserUp(
                new User(
                        request.getName(),
                        request.getLastName(),
                        request.getSecondName(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getSecretQuestion(),
                        request.getSecretQuestionAnswer(),
                        UserRole.USER

                )

        );
        sendConfirmationToken(request);
        Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());
        User user = userOptional.get();

        String json = new JSONObject()
                .put("status", "success")
                .put("email", user.getEmail())
                .toString();
        return json;

    }

    public String sendConfirmationToken(RegistrationRequest request){
        Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());
        boolean exists = userOptional.isPresent();
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user found for this email.")
                    .toString();
            return json;
        }
        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(60),user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);


        EmailTemplates emailTemplates = new EmailTemplates(user.getName(),token);
        emailSender.send(request.getEmail(), emailTemplates.buildRegistrationEmail());
        String json = new JSONObject()
                .put("status", "success")
                .put("message", "Please check your email for the new confirmation token.")
                .toString();
        return json;

    }

    @Transactional
    public String confirmToken(VerificationRequest request) {
        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository
                .findByToken(request.getToken());
        boolean exists = userRepository.findUserByEmail(request.getEmail()).isPresent();
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user found for this email.")
                    .toString();
            return json;

        }
        boolean tokenExists = confirmationTokenOptional.isPresent();
        if(!tokenExists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Entered token is invalid.")
                    .toString();
            return json;

        }

        ConfirmationToken confirmationToken = confirmationTokenOptional.get();

        boolean isAlreadyConfirmed = confirmationTokenService.checkConfirmationTokens(request.getEmail());
        if (isAlreadyConfirmed == true) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Entered email is already verified.")
                    .toString();
            return json;
        }


        LocalDateTime expiredAt = confirmationToken.getExpirationTime();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Entered token is expired.")
                    .toString();
            return json;
        }
        List<ConfirmationToken> confirmationTokens = confirmationTokenRepository.findConfirmationTokenByUser_Email(request.getEmail());
        List<LocalDateTime> creationTimes = confirmationTokens.stream().map(ConfirmationToken::getCreateTime).collect(Collectors.toList());
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime minTime = LocalDateTime.MIN;
        boolean isLatestToken = false;
        for(LocalDateTime l : creationTimes){
            if(l.isAfter(minTime)){
                minTime = l ;
            }
            localDateTime = minTime;

        }if(localDateTime.isEqual(confirmationToken.getCreateTime())){


        confirmationTokenService.setConfirmationTime(request.getToken());
        userService.enableUser(
                confirmationToken.getUser().getEmail());
        String json = new JSONObject()
                .put("status", "success")
                .put("verifiedByEmail", true)
                .toString();
        return json;
        }
        String json = new JSONObject()
                .put("status", "failure")
                .put("errorMessage", "Entered token is expired.")
                .toString();
        return json;
    }

    public String forgotPassword(PasswordRequest request) {
        String secretQuestion = request.getSecretQuestion();
        String secretQuestionAnswer = request.getSecretQuestionAnswer();
        String email = request.getEmail();

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        boolean exists = userOptional.isPresent();

        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user found for this email.")
                    .toString();
            return json;
        }
        User user = userOptional.get();
        if(!(secretQuestion.equals(user.getSecretQuestion())
                && secretQuestionAnswer.equals(user.getSecretQuestionAnswer()))){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Secret question and answer do not match for this user.")
                    .toString();
            return json;
        }
        //Creating random password of 6 characters.
        String Upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String num = "0123456789";
        String lower = "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb0 = new StringBuilder(3);
        StringBuilder sb1 = new StringBuilder(3);
        StringBuilder sb2 = new StringBuilder(3);
        String sb3 = "!?.";


        for(int i=0;i<3;i++){
            int index = (int)(Upper.length() * Math.random());
            int a = (int)(lower.length() * Math.random());
            int b = (int)(num.length() * Math.random());

            sb0.append(Upper.charAt(index));
            sb1.append(lower.charAt(a));
            sb2.append(num.charAt(b));

        }
        String password = sb0.toString() + sb1.toString() + sb2.toString() + sb3;
        String newPasswordEncoded = bCryptPasswordEncoder.encode(password);
        user.setPassword(newPasswordEncoded);
        userRepository.save(user);

        EmailTemplates emailTemplates = new EmailTemplates(password);
        emailSender.send(email,emailTemplates.buildForgotPasswordEmail());
        String json = new JSONObject()
                .put("status", "success")
                .put("successMessage", "Your password has been sent to your email. Please don't forget to check your spam folder as well.")
                .toString();
        return json;
    }
    public String updatePassword(UpdatePasswordRequest request) {
        String secretQuestion = request.getSecretQuestion();
        String secretQuestionAnswer = request.getSecretQuestionAnswer();
        String email = request.getEmail();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        Optional<User> userOptional = userRepository.findUserByEmail(email);
        boolean exists = userOptional.isPresent();
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Password could not be changed.")
                    .toString();
            return json;
        }
        User user = userOptional.get();
        System.out.println("Old Passssss" + oldPassword + " New PASSSSS" + newPassword);
        boolean matches = bCryptPasswordEncoder.matches(oldPassword,user.getPassword());

        if(!(secretQuestion.equals(user.getSecretQuestion()))
                || !(secretQuestionAnswer.equals(user.getSecretQuestionAnswer()))
                || !matches)
        {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Entered values do not match for this user..")
                    .toString();
            return json;
        }
        boolean tokenMatch = request.getToken().equals(user.getSessionToken().getToken());
        if(!tokenMatch){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Token expired.Please logout and re-login.")
                    .toString();
            return json;
        }

        String newPasswordEncoded = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(newPasswordEncoded);
        userRepository.save(user);
        String token = sessionTokenRepository.findSessionTokenByUser_Email(user.getEmail()).getToken();

        String json = new JSONObject()
                .put("status", "success")
                .put("message", "Your password has been changed successfully.")
                .toString();
        return json;

    }

    public String contactUs(ContactUsRequest request) {
        String name = request.getName();
        String surname = request.getSurname();
        String email = request.getEmail();
        String senderEmail = request.getSenderEmail();
        String subject = request.getSubject();
        String content = request.getContent();

        emailSender.sendContactUs(email,name,surname,senderEmail,subject,content);
        String json = new JSONObject()
                .put("status", "success")
                .put("successMessage", "Thank you for contacting MyNotesApp team, we will respond in 1-3 business days.")
                .toString();
        return json;
    }
}
