package com.seniordesign.v02.noteCategory;

import com.seniordesign.v02.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/note_category")
public class NoteCategoryController {

    private final NoteCategoryService noteCategoryService;

    @Autowired
    public NoteCategoryController(NoteCategoryService noteCategoryService) {
        this.noteCategoryService = noteCategoryService;
    }

    @PostMapping(path = "gNoteCategories")
    public String getNoteCategories(@RequestBody NoteCategoryRequest noteCategoryRequest) {
        return noteCategoryService.getNoteCategories(noteCategoryRequest);
    }
    @PostMapping(path = "cNoteCategory")
    public String createNoteCategory(@RequestBody NoteCategoryRequest noteCategoryRequest) {
        return noteCategoryService.createNoteCategory(noteCategoryRequest);
    }

    @PostMapping(path = "uNoteCategory")
    public String updateNoteCategory(@RequestBody NoteCategoryRequest noteCategoryRequest){
        return noteCategoryService.updateNoteCategory(noteCategoryRequest);
    }
    @PostMapping(path = "dNoteCategory")
    public String deleteNoteCategory(@RequestBody NoteCategoryRequest noteCategoryRequest){
        return noteCategoryService.deleteNoteCategory(noteCategoryRequest);
    }




}
