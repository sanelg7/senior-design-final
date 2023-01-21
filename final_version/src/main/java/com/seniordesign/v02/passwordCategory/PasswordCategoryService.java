package com.seniordesign.v02.passwordCategory;

import com.seniordesign.v02.noteCategory.NoteCategory;
import com.seniordesign.v02.user.User;
import com.seniordesign.v02.user.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PasswordCategoryService {
    private final PasswordCategoryRepository passwordCategoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public PasswordCategoryService(PasswordCategoryRepository passwordCategoryRepository, UserRepository userRepository) {
        this.passwordCategoryRepository = passwordCategoryRepository;
        this.userRepository = userRepository;
    }

    public String getPasswordCategories(PasswordCategoryRequest passwordCategoryRequest) {
        List<PasswordCategory> passwordCategoryByUserEmail =
                passwordCategoryRepository.findPasswordByUser_Email(passwordCategoryRequest.getEmail());
        Optional<User> userOptional = userRepository.findUserByEmail(passwordCategoryRequest.getEmail());
        boolean exists = userOptional.isPresent();
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage","User not found.").toString();
            return json;

        }
        User actualUser = userOptional.get();
        boolean tokenMatch = passwordCategoryRequest.getToken().equals(actualUser.getSessionToken().getToken());
        if(tokenMatch) {
            JSONObject theObject = new JSONObject();
            theObject.put("status" , "success");
            JSONObject json = new JSONObject();
            JSONArray categoryJson = new JSONArray();
            for (PasswordCategory a : passwordCategoryByUserEmail) {
                json.put("passwordCategoryID", a.getPasswordCategoryID());
                json.put("passwordCategoryTitle", a.getPasswordCategoryName());

                categoryJson.put(json);
                json = new JSONObject();

            }
            theObject.put("passwordCategories",categoryJson);
            return theObject.toString();
        }
        else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage","Your session has expired.Please logout and re-login..").toString();
            return json;
        }
    }

    public String createPasswordCategory(PasswordCategoryRequest passwordCategoryRequest) {

        String email = passwordCategoryRequest.getEmail();
        String passwordCategoryName = passwordCategoryRequest.getPasswordCategoryTitle();
        List<String> passwordCategoryByUserEmail = passwordCategoryRepository.findPasswordByUser_Email(email).stream().map(PasswordCategory::getPasswordCategoryName).collect(Collectors.toList());
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user with this email found.")
                    .toString();
            return json;
        } else {
            User user = userOptional.get();
            if (passwordCategoryByUserEmail.contains(passwordCategoryName)) {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Password category name already exists.")
                        .toString();
                return json;

            }
            PasswordCategory category = new PasswordCategory(user, passwordCategoryName);
            boolean tokenMatch = passwordCategoryRequest.getToken().equals(category.getUser().getSessionToken().getToken());
            if (tokenMatch) {
                passwordCategoryRepository.save(category);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("message", "Password category has been created successfully.")
                        .toString();
                return json;
            } else {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Your session has expired.Please logout and re-login..")
                        .toString();
                return json;
            }

        }

    }

    public String deletePasswordCategory(PasswordCategoryRequest passwordCategoryRequest) {
        Long passwordCategoryID = passwordCategoryRequest.getPasswordCategoryID();
        PasswordCategory passwordCategory = passwordCategoryRepository
                .findPasswordCategoryByPasswordCategoryID(passwordCategoryID);


        if (passwordCategory==null) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("message", "No password category found to delete.")
                    .toString();
            return json;
        }
        boolean tokenMatch = passwordCategoryRequest.getToken().equals
                (passwordCategory.getUser().getSessionToken().getToken());

        if(tokenMatch) {
            passwordCategoryRepository.deleteById(passwordCategoryID);
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Password Category has been deleted successfully.")
                    .toString();
            return json;
        }else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Your session has expired.Please logout and re-login..")
                    .toString();
            return json;
        }

    }

    @Transactional
    public String updatePasswordCategory(PasswordCategoryRequest passwordCategoryRequest) {
        Optional<User> userOptional = userRepository.findUserByEmail(passwordCategoryRequest.getEmail());
        boolean exists = passwordCategoryRepository.existsById(passwordCategoryRequest.getPasswordCategoryID());
        if (!exists) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No password category found to update.")
                    .toString();
            return json;
        }
        boolean userExists = userOptional.isPresent();
        if (!userExists) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user found for this category.")
                    .toString();
            return json;
        }
        User user = userOptional.get();
        Long passwordCategoryID = passwordCategoryRequest.getPasswordCategoryID();
        String newName = passwordCategoryRequest.getPasswordCategoryTitle();

        List passwordCategories = passwordCategoryRepository.findPasswordByUser_Email(user.getEmail()).stream().map(PasswordCategory::getPasswordCategoryName).collect(Collectors.toList());
        boolean tokenMatch = passwordCategoryRequest.getToken().equals(user.getSessionToken().getToken());
        if (tokenMatch) {
            if (!(passwordCategories.contains(newName))) {
                passwordCategoryRepository.findPasswordCategoryByPasswordCategoryID
                        (passwordCategoryID).setPasswordCategoryName(newName);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("message", "Password category has been updated successfully.")
                        .toString();
                return json;
            } else {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Password category with this name already exists.")
                        .toString();
                return json;
            }

        }else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Your session has expired.Please logout and re-login..")
                    .toString();
            return json;
        }


    }

}