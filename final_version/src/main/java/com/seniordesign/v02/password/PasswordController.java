package com.seniordesign.v02.password;

import com.seniordesign.v02.note.Note;
import com.seniordesign.v02.password.secure_key.ForgotSecureKeyRequest;
import com.seniordesign.v02.password.secure_key.UpdateSecureKeyRequest;
import com.seniordesign.v02.passwordCategory.PasswordCategory;
import com.seniordesign.v02.passwordCategory.PasswordCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/password")
public class PasswordController {
    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService){
            this.passwordService = passwordService;
    }


    @PostMapping(path = "cPasswd")
    public String createPassword(@RequestBody PasswordRequest request){
        return passwordService.createPassword(request);
    }

    @PostMapping(path = "uPasswd")
    public String updatePassword(@RequestBody PasswordRequest request){
        return passwordService.updatePassword(request);
    }
    @PostMapping(path = "dPasswd")
    public String deletePassword(@RequestBody PasswordRequest request){
        return passwordService.deletePassword(request);
    }

    //Get all passwords under a category.
    @PostMapping(path = "gPasswds")
    public String getPasswords(@RequestBody PasswordRequest passwordRequest){
        return passwordService.getPasswords(passwordRequest);
    }
    //Only content related requests.
    @PostMapping(path = "gContent")
    public String getPasswordContent(@RequestBody PasswordRequest request){
        return passwordService.getPasswordContent(request);
    }
    @PostMapping(path = "uContent")
    public String updatePasswordContent(@RequestBody PasswordRequest request){
        return passwordService.updatePasswordContent(request);
    }
    //Should only take id and secure key itself to check.
    @PostMapping(path = "cSecureKey")
    public String confirmSecureKey(@RequestBody PasswordRequest request){
        return passwordService.confirmSecureKey(request);
    }
    //SecureKey ops.
    @PostMapping(path = "fSecureKey")
    public String forgotSecureKey(@RequestBody ForgotSecureKeyRequest request){
        return passwordService.forgotSecureKey(request);
    }
    @PostMapping(path = "uSecureKey")
    public String updateSecureKey(@RequestBody UpdateSecureKeyRequest request){
        return passwordService.updateSecureKey(request);
    }



}
