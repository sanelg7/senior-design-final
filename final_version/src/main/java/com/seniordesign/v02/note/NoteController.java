package com.seniordesign.v02.note;

import com.seniordesign.v02.noteCategory.NoteCategory;
import com.seniordesign.v02.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/note")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService){
        this.noteService=noteService;
    }


    //Only content related requests.
    @PostMapping(path = "uNoteContent")
    public String updateNoteContent(@RequestBody NoteRequest request){
        return noteService.updateNoteContent(request);
    }
    @PostMapping(path = "gNoteContent")
    public String getNoteContent(@RequestBody NoteRequest request){
        return noteService.getNoteContent(request);
    }




    @PostMapping(path = "cNote")
    public String createNote(@RequestBody NoteRequest request){
        return noteService.addNote(request);
    }


    @PostMapping(path = "uNote")
    public String updateNote(@RequestBody NoteRequest request){
        return noteService.updateNote(request);
    }

    @PostMapping(path = "dNote")
    public String deleteNote(@RequestBody NoteRequest request){
        return noteService.deleteNote(request);
    }

    @PostMapping(path = "gNotes")
    public String getNotes(@RequestBody NoteRequest request){
        return noteService.getNotes(request);
    }

}
