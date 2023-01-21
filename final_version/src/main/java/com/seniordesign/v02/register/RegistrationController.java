package com.seniordesign.v02.register;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    @RequestMapping(path = "/register")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    //manual verification
    @RequestMapping(path = "/vEmail")
    @PostMapping
    public String verifyEmail(@RequestBody VerificationRequest request){
        return registrationService.confirmToken(request);
    }

    @RequestMapping(path = "/sEmail")
    @PostMapping
    public String sendConfirmationToken(@RequestBody RegistrationRequest request){
        return registrationService.sendConfirmationToken(request);
    }

    @RequestMapping(path = "/fPasswd")
    @PostMapping
    public String forgotPassword(@RequestBody PasswordRequest request){
        return registrationService.forgotPassword(request);
    }

    @RequestMapping(path = "/uLoginPasswd")
    @PostMapping
    public String updatePassword(@RequestBody UpdatePasswordRequest request){
        return registrationService.updatePassword(request);
    }


    @RequestMapping(path = "/contactUs")
    @PostMapping
    public String contactUs(@RequestBody ContactUsRequest request){
       return registrationService.contactUs(request);
    }


}
