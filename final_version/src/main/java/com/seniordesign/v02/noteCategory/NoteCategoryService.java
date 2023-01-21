package com.seniordesign.v02.noteCategory;

import com.seniordesign.v02.activity.Activity;
import com.seniordesign.v02.note.Note;
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
public class NoteCategoryService {
    private final NoteCategoryRepository noteCategoryRepository;
    private final UserRepository userRepository;
    @Autowired
    public NoteCategoryService(NoteCategoryRepository noteCategoryRepository, UserRepository userRepository){
        this.noteCategoryRepository = noteCategoryRepository;
        this.userRepository = userRepository;
    }

    public String getNoteCategories(NoteCategoryRequest noteCategoryRequest){
        List<NoteCategory> noteCategoryByUserEmail = noteCategoryRepository.findNoteCategoryByUser_Email(noteCategoryRequest.getEmail());
        Optional<User> userOptional = userRepository.findUserByEmail(noteCategoryRequest.getEmail());
        boolean exists = userOptional.isPresent();
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage","User not found.").toString();
            return json;

        }
        User actualUser = userOptional.get();
        boolean tokenMatch = noteCategoryRequest.getToken().equals(actualUser.getSessionToken().getToken());
        if(tokenMatch) {
            JSONObject theObject = new JSONObject();
            theObject.put("status" , "success");
            JSONObject json = new JSONObject();
            JSONArray categoryJson = new JSONArray();
            for (NoteCategory a : noteCategoryByUserEmail) {
                json.put("noteCategoryID", a.getNoteCategoryID());
                json.put("noteCategoryTitle", a.getCategoryName());

                categoryJson.put(json);
                json = new JSONObject();

            }
            theObject.put("noteCategories",categoryJson);
            return theObject.toString();
        }

        else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage","Your session has expired.Please logout and re-login..").toString();
            return json;
        }

    }

    public String createNoteCategory(NoteCategoryRequest noteCategoryRequest){
        String email = noteCategoryRequest.getEmail();
        String categoryName = noteCategoryRequest.getNoteCategoryTitle();
        List<String> noteCategoryByUserEmail =
                noteCategoryRepository.findNoteCategoryByUser_Email(email)
                        .stream().map(NoteCategory::getCategoryName).collect(Collectors.toList());
        Optional<User> userOptional = userRepository.findUserByEmail(email);

        if(!userOptional.isPresent()){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user with this email found.")
                    .toString();
            return json;
        }else{
            User user = userOptional.get();

            if(noteCategoryByUserEmail.contains(categoryName)){
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Another category with this name already exists.")
                        .toString();
                return json;
            }
            NoteCategory category;
            category = new NoteCategory(user, noteCategoryRequest.getNoteCategoryTitle());

            boolean tokenMatch = noteCategoryRequest.getToken().equals(category.getUser().getSessionToken().getToken());
            if(tokenMatch) {
                noteCategoryRepository.save(category);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("message", "Category has been created successfully.")
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

    }
    public String deleteNoteCategory(NoteCategoryRequest noteCategoryRequest){
        Long noteCategoryId = noteCategoryRequest.getNoteCategoryID();
        NoteCategory noteCategory = noteCategoryRepository.findNoteCategoryByNoteCategoryID(noteCategoryId);
        boolean tokenMatch = noteCategoryRequest.getToken().equals(noteCategory.getUser().getSessionToken().getToken());
        boolean exists = noteCategoryRepository.existsById(noteCategoryId);
        if(!exists){
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "No category found to delete.")
                    .toString();
            return json;
        }
        if(tokenMatch) {
            noteCategoryRepository.deleteById(noteCategoryId);
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Category has been deleted successfully.")
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
    public String updateNoteCategory(NoteCategoryRequest noteCategoryRequest){
        Optional<User> userOptional = userRepository.findUserByEmail(noteCategoryRequest.getEmail());
        boolean exists = noteCategoryRepository.existsById(noteCategoryRequest.getNoteCategoryID());
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No category found to update.")
                    .toString();
            return json;
        }
        boolean userExists = userOptional.isPresent();
        if(!userExists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No user found.")
                    .toString();
            return json;
        }
        User user = userOptional.get();
        Long noteCategoryId = noteCategoryRequest.getNoteCategoryID();
        String newName;
        newName = noteCategoryRequest.getNoteCategoryTitle();
        boolean tokenMatch = noteCategoryRequest.getToken().equals(user.getSessionToken().getToken());

        List noteCategories = noteCategoryRepository.findNoteCategoryByUser_Email(user.getEmail()).stream().map(NoteCategory::getCategoryName).collect(Collectors.toList());
        if(tokenMatch) {
            if(!(noteCategories.contains(newName))){
                noteCategoryRepository.findNoteCategoryByNoteCategoryID(noteCategoryId).setCategoryName(newName);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("message", "Category has been updated successfully.")
                        .toString();
                return json;
            }else{
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Note category with this name already exists.")
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
