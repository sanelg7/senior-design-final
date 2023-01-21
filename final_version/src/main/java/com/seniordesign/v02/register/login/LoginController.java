package com.seniordesign.v02.register.login;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @RequestMapping(path = "/uLogout")
    @PostMapping
    public String logout(@RequestBody LogoutRequest logoutRequest){
        return loginService.logout(logoutRequest);
    }
    @RequestMapping(path = "/login")
    @PostMapping
    public String login(@RequestBody LoginRequest loginRequest){
        return loginService.login(loginRequest);
    }


}
