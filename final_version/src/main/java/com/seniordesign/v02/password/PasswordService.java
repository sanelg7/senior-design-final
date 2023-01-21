package com.seniordesign.v02.password;

import com.seniordesign.v02.password.secure_key.ForgotSecureKeyRequest;
import com.seniordesign.v02.password.secure_key.UpdateSecureKeyRequest;
import com.seniordesign.v02.passwordCategory.PasswordCategory;
import com.seniordesign.v02.passwordCategory.PasswordCategoryRepository;
import com.seniordesign.v02.user.User;
import com.seniordesign.v02.user.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PasswordService {

    private final PasswordCategoryRepository passwordCategoryRepository;
    private final PasswordRepository passwordRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;


    public PasswordService(PasswordRepository passwordRepository, PasswordCategoryRepository passwordCategoryRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.passwordCategoryRepository = passwordCategoryRepository;
        this.passwordRepository = passwordRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    public String getPasswords(PasswordRequest request) {
        PasswordCategory category = passwordCategoryRepository
                .findPasswordCategoryByPasswordCategoryID(request.getPasswordCategoryID());
        if ((category == null)) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No such password category found.").toString();
            return json;
        }
        List<Password> passwords = category.getPasswords().stream().collect(Collectors.toList());

        boolean tokenMatch = request.getToken()
                .equals(category.getUser().getSessionToken().getToken());
        if (tokenMatch) {
            JSONObject theObject = new JSONObject();
            theObject.put("status", "success");
            JSONObject json = new JSONObject();
            JSONArray activityJson = new JSONArray();
            for (Password a : passwords) {
                json.put("passwordID", a.getPasswordID());
                json.put("passwordTitle", a.getPasswordTitle());
                json.put("passwordDescription", a.getPasswordDescription());
                json.put("secure", a.isSecure());

                activityJson.put(json);
                json = new JSONObject();
            }
            theObject.put("passwords", activityJson);
            return theObject.toString();
        } else {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Could not fetch passwords for this category.")
                    .toString();
            return json;
        }
    }

    public String createPassword(PasswordRequest request) {

        PasswordCategory passwordCategory = passwordCategoryRepository
                .findPasswordCategoryByPasswordCategoryID(request.getPasswordCategoryID());
        if (passwordCategory == null) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No password category found to bind this password.")
                    .toString();
            return json;
        } else {
            Set<String> title = passwordCategory.getPasswords()
                    .stream().map(Password::getPasswordTitle).collect(Collectors.toSet());
            Set<String> description = passwordCategory.getPasswords()
                    .stream().map(Password::getPasswordDescription).collect(Collectors.toSet());
            if (!(title.contains(request.getPasswordTitle()) && description.contains(request.getPasswordDescription()))) {
                String passwordTitle = request.getPasswordTitle();
                String passwordDescription = request.getPasswordDescription();
                String encodedSecureKey = bCryptPasswordEncoder.encode(request.getSecureKey());
                boolean secure = request.isSecure();
                Password password = new Password(passwordTitle, passwordDescription, secure, encodedSecureKey, passwordCategory);
                boolean tokenMatch = request.getToken().equals(passwordCategory.getUser().getSessionToken().getToken());
                if (tokenMatch) {
                    passwordRepository.save(password);
                    String json = new JSONObject()
                            .put("status", "success")
                            .put("message", "Password has been created successfully.")
                            .toString();
                    return json;
                } else {
                    String json = new JSONObject()
                            .put("status", "failure")
                            .put("errorMessage", "Your session has expired.Please logout and re-login..")
                            .toString();
                    return json;
                }
            } else {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Note name and description combination must be unique.")
                        .toString();
                return json;
            }
        }
    }

    public String deletePassword(PasswordRequest request) {
        boolean exists = passwordRepository.existsById(request.getPasswordID());

        if (!exists) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No such password found to delete.")
                    .toString();
            return json;
        }
        Password password = passwordRepository.findPasswordByPasswordID(request.getPasswordID());

        boolean tokenMatch = request.getToken().equals(password.getPasswordCategory()
                .getUser().getSessionToken().getToken());
        if (tokenMatch) {
            passwordRepository.deleteById(request.getPasswordID());
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Password has been deleted successfully.")
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

    @Transactional
    public String updatePassword(PasswordRequest passwordRequest) {
        Long passwordId = passwordRequest.getPasswordID();
        PasswordCategory passwordCategoryOptional = passwordCategoryRepository.findPasswordCategoryByPasswords_PasswordID(passwordRequest.getPasswordID());
        if (passwordCategoryOptional == null) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No password found for this password.")
                    .toString();
            return json;
        }
        Set<Password> passwordByCategory = passwordCategoryRepository
                .findPasswordCategoryByPasswords_PasswordID(passwordId).getPasswords();
        Set<String> name = passwordByCategory
                .stream().map(Password::getPasswordTitle).collect(Collectors.toSet());

        Set<String> description = passwordByCategory.stream()
                .map(Password::getPasswordDescription).collect(Collectors.toSet());
        boolean secure = passwordRequest.isSecure();
        Password oldPassword = passwordRepository.findPasswordByPasswordID(passwordRequest.getPasswordID());


        boolean tokenMatch = passwordRequest.getToken()
                .equals(oldPassword.getPasswordCategory().getUser().getSessionToken().getToken());
        if (tokenMatch) {
            if (oldPassword.isSecure() == passwordRequest.isSecure()) {
                if (!(name.contains(passwordRequest.getPasswordTitle()) &&
                        description.contains(passwordRequest.getPasswordDescription()))) {
                    oldPassword.setPasswordTitle(passwordRequest.getPasswordTitle());
                    oldPassword.setPasswordDescription(passwordRequest.getPasswordDescription());
                    passwordRepository.save(oldPassword);
                    String json = new JSONObject()
                            .put("status", "success")
                            .put("message", "Password title and description were updated succesfully.")
                            .toString();
                    return json;
                }
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Nothing to save.")
                        .toString();
                return json;

            }
            if (!(name.contains(passwordRequest.getPasswordTitle()) &&
                    description.contains(passwordRequest.getPasswordDescription()))) {
                if (!oldPassword.isSecure()) {
                    oldPassword.setPasswordTitle(passwordRequest.getPasswordTitle());
                    oldPassword.setPasswordDescription(passwordRequest.getPasswordDescription());
                    oldPassword.setSecure(secure);
                    String encodedSecureKey = bCryptPasswordEncoder.encode(passwordRequest.getSecureKey());
                    oldPassword.setSecureKey(encodedSecureKey);
                    passwordRepository.save(oldPassword);
                    String json = new JSONObject()
                            .put("status", "success")
                            .put("message", "Password has been updated successfully.")
                            .toString();
                    return json;
                } else {
                    oldPassword.setPasswordTitle(passwordRequest.getPasswordTitle());
                    oldPassword.setPasswordDescription(passwordRequest.getPasswordDescription());
                    oldPassword.setSecure(secure);
                    oldPassword.setSecureKey(null);
                    passwordRepository.save(oldPassword);

                    String json = new JSONObject()
                            .put("status", "success")
                            .put("message", "Password is not secure anymore.")
                            .toString();
                    return json;
                }

            }
            if (!oldPassword.isSecure()) {
                oldPassword.setSecure(secure);
                String encodedSecureKey = bCryptPasswordEncoder.encode(passwordRequest.getSecureKey());
                oldPassword.setSecureKey(encodedSecureKey);
                passwordRepository.save(oldPassword);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("message", "Password was made secure.")
                        .toString();
                return json;
            }
            oldPassword.setPasswordTitle(passwordRequest.getPasswordTitle());
            oldPassword.setPasswordDescription(passwordRequest.getPasswordDescription());
            oldPassword.setSecure(secure);
            oldPassword.setSecureKey(null);
            passwordRepository.save(oldPassword);

            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Password is not secure anymore.")
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


    public String getPasswordContent(PasswordRequest request) {
        boolean exists = passwordRepository.existsById(request.getPasswordID());
        if (!exists) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No such password content found.")
                    .toString();
            return json;
        }
        PasswordCategory category = passwordCategoryRepository
                .findPasswordCategoryByPasswords_PasswordID(request.getPasswordID());
        boolean tokenMatch = request.getToken().equals(category.getUser().getSessionToken().getToken());
        if (tokenMatch) {
            Password password = passwordRepository.findPasswordByPasswordID(request.getPasswordID());
            if (password.getPasswordContent() != null) {
                String passwordContent = password.getPasswordContent();
                String json = new JSONObject()
                        .put("status", "success")
                        .put("passwordContent", passwordContent)
                        .toString();
                return json;
            }
            String json = new JSONObject()
                    .put("status", "success")
                    .put("passwordContent", "")
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

    public String updatePasswordContent(PasswordRequest request) {
        PasswordCategory category = passwordCategoryRepository
                .findPasswordCategoryByPasswords_PasswordID(request.getPasswordID());
        if (category == null) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No password found to update.")
                    .toString();
            return json;
        }
        boolean tokenMatch = request.getToken()
                .equals(category.getUser().getSessionToken().getToken());
        if (tokenMatch) {
            Password password = passwordRepository.findPasswordByPasswordID(request.getPasswordID());
            if (password == null) {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "No password found to update.")
                        .toString();
                return json;

            }

            password.setPasswordContent(request.getPasswordContent());

            passwordRepository.save(password);
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Password content has been saved successfully.")
                    .put("passwordContent", request.getPasswordContent())
                    .toString();
            return json;
        }
        String json = new JSONObject()
                .put("status", "failure")
                .put("errorMessage", "Your session has expired.Please logout and re-login..")
                .toString();
        return json;
    }

    public String confirmSecureKey(PasswordRequest request) {
        Password password = passwordRepository.findPasswordByPasswordID(request.getPasswordID());
        if (password == null) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No password found.")
                    .toString();
            return json;
        }
        boolean matches = bCryptPasswordEncoder.matches(request.getSecureKey(), password.getSecureKey());
        boolean tokenMatch = request.getToken()
                .equals(password.getPasswordCategory().getUser().getSessionToken().getToken());
        if (tokenMatch) {
            if (!matches) {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Secure key check failed.")
                        .toString();
                return json;
            }
            String json = new JSONObject()
                    .put("status", "success")
                    .toString();
            return json;
        }
        String json = new JSONObject()
                .put("status", "failure")
                .put("errorMessage", "Your session has expired.Please logout and re-login..")
                .toString();
        return json;
    }

    public String forgotSecureKey(ForgotSecureKeyRequest request) {
        Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());
        boolean exists = userOptional.isPresent();
        if (!exists) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No such user found.")
                    .toString();
            return json;
        }
        User user = userOptional.get();

        boolean tokenMatch = request.getToken()
                .equals(user.getSessionToken().getToken());
        boolean matches = bCryptPasswordEncoder.matches(request.getUserPassword(), user.getPassword());
        if (tokenMatch) {
            if (user.getSecretQuestion().equals(request.getSecretQuestion()) &&
                    user.getSecretQuestionAnswer().equals(request.getSecretQuestionAnswer())
                    && matches) {
                Password password = passwordRepository.findPasswordByPasswordID(request.getPasswordID());
                String encodedSecureKey = bCryptPasswordEncoder.encode(request.getNewSecureKey());
                password.setSecureKey(encodedSecureKey);
                passwordRepository.save(password);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("message", "Secure key changed successfully.")
                        .toString();
                return json;

            }
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Credentials check failed.")
                    .toString();
            return json;

        }
        String json = new JSONObject()
                .put("status", "failure")
                .put("errorMessage", "Your session has expired.Please logout and re-login..")
                .toString();
        return json;


    }

    public String updateSecureKey(UpdateSecureKeyRequest request) {
        Password password = passwordRepository.findPasswordByPasswordID(request.getPasswordID());
        if (password == null) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No password found.")
                    .toString();
            return json;
        }
        boolean matches = bCryptPasswordEncoder
                .matches(request.getOldSecureKey(), password.getSecureKey());
        boolean tokenMatch = request.getToken()
                .equals(password.getPasswordCategory().getUser().getSessionToken().getToken());
        if (tokenMatch) {
            if (!matches) {
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Secure key check failed.")
                        .toString();
                return json;
            }
            String encodedSecureKey = bCryptPasswordEncoder.encode(request.getNewSecureKey());
            password.setSecureKey(encodedSecureKey);
            passwordRepository.save(password);
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Secure key updated successfully.")
                    .toString();
            return json;
        }
        String json = new JSONObject()
                .put("status", "failure")
                .put("errorMessage", "Your session has expired.Please logout and re-login..")
                .toString();
        return json;
    }
}
