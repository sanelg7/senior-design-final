package com.seniordesign.v02.passwordCategory;

import com.seniordesign.v02.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/password_category")
public class PasswordCategoryController {

    private final PasswordCategoryService passwordCategoryService;

    @Autowired
    public PasswordCategoryController(PasswordCategoryService passwordCategoryService){
        this.passwordCategoryService = passwordCategoryService;
    }

    @PostMapping(path = "/gPasswdCategories")
    public String getPasswordCategories(@RequestBody PasswordCategoryRequest passwordCategoryRequest){
        return passwordCategoryService.getPasswordCategories(passwordCategoryRequest);
    }
    @PostMapping(path = "/cPasswdCategory")
    public String createPasswordCategory(@RequestBody PasswordCategoryRequest passwordCategoryRequest){
        return passwordCategoryService.createPasswordCategory(passwordCategoryRequest);
    }
    @PostMapping(path = "/uPasswdCategory")
    public String updatePasswordCategory(@RequestBody PasswordCategoryRequest passwordCategoryRequest){
        return passwordCategoryService.updatePasswordCategory(passwordCategoryRequest);
    }
    @PostMapping(path = "/dPasswdCategory")
    public String deletePasswordCategory(@RequestBody PasswordCategoryRequest passwordCategoryRequest){
        return passwordCategoryService.deletePasswordCategory(passwordCategoryRequest);
    }
}
