package com.seniordesign.v02.note;

import com.seniordesign.v02.noteCategory.NoteCategory;
import com.seniordesign.v02.noteCategory.NoteCategoryRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteCategoryRepository noteCategoryRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, NoteCategoryRepository noteCategoryRepository){
        this.noteRepository=noteRepository;
        this.noteCategoryRepository = noteCategoryRepository;
    }

    public String getNotes(NoteRequest request){
        NoteCategory category = noteCategoryRepository
                .findNoteCategoryByNoteCategoryID(request.getNoteCategoryID());
        if((category==null)){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage","No such note category found.").toString();
            return json;
        }

        List<Note> notes = category.getNotes().stream().collect(Collectors.toList());

        boolean tokenMatch = request.getToken().equals(category.getUser().getSessionToken().getToken());
        if(tokenMatch) {
            JSONObject theObject = new JSONObject();
            theObject.put("status" , "success");
            JSONObject json = new JSONObject();
            JSONArray noteJson = new JSONArray();
            for (Note a : notes) {
                json.put("noteID", a.getNoteID());
                json.put("noteTitle", a.getNoteName());
                json.put("noteDescription", a.getNoteDescription());

                noteJson.put(json);
                json = new JSONObject();
            }
            theObject.put("notes",noteJson);
            return theObject.toString();
        }else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage","Could not fetch notes for this category.").toString();
            return json;
        }
    }

    public String addNote(NoteRequest request){
        //Checking if user has same note name under category
        NoteCategory category = noteCategoryRepository.findNoteCategoryByNoteCategoryID(request.getNoteCategoryID());
        if(category == null){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No category found to bind this note.")
                    .toString();
            return json;
        }else{
         Set<String> name = category.getNotes().
                 stream().map(Note::getNoteName).collect(Collectors.toSet());
         Set<String> description = category.getNotes().
                 stream().map(Note::getNoteDescription)  .collect(Collectors.toSet());
             if(!(name.contains(request.getNoteTitle()) && description.contains(request.getNoteDescription()))){

                 String noteName = request.getNoteTitle();
                 String noteTextContent = "Enter your notes here...";
                 Note newNote = new Note(noteName,noteTextContent, category,request.getNoteDescription());
                 boolean tokenMatch = request.getToken().equals(category.getUser().getSessionToken().getToken());
                 if(tokenMatch) {
                     noteRepository.save(newNote);
                     String json = new JSONObject()
                             .put("status", "success")
                             .put("message", "Note has been created successfully.")
                             .toString();
                     return json;
                 }else{
                     String json = new JSONObject()
                             .put("status", "failure")
                             .put("errorMessage", "Your session has expired.Please logout and re-login..")
                             .toString();
                     return json;
                 }

             }else{
                 String json = new JSONObject()
                         .put("status", "failure")
                         .put("errorMessage", "Note name and description combination must be unique.")
                         .toString();
                 return json;         }
     }

    }

    public String deleteNote(NoteRequest request){
        Long noteId = request.getNoteID();
        boolean exists = noteRepository.existsById(noteId);

        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No such note found to delete.")
                    .toString();
            return json;
        }
        Note note = noteRepository.findNoteByNoteID(request.getNoteID());

        boolean tokenMatch = request.getToken().equals(note.getNoteCategory()
                .getUser().getSessionToken().getToken());
        if(tokenMatch) {
            noteRepository.deleteById(noteId);
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Note has been deleted successfully.")
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
    public String updateNote(NoteRequest request){
        Long noteId = request.getNoteID();
        NoteCategory category = noteCategoryRepository.findNoteCategoryByNotes_NoteID(noteId);
        if(category==null){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("message", "Could not find note category related to this note.")
                    .toString();
            return json;
        }
        Set<Note> notesByCategory = category.getNotes();

        Set<String> name = notesByCategory.stream().map(Note::getNoteName).collect(Collectors.toSet());
        Set<String> description = notesByCategory.stream().map(Note::getNoteDescription).collect(Collectors.toSet());

        Note oldNote = noteRepository.findNoteByNoteID(request.getNoteID());
        if(!(name.contains(request.getNoteTitle()) &&
                description.contains(request.getNoteDescription()))){
            boolean tokenMatch = request.getToken()
                    .equals(oldNote.getNoteCategory().getUser().getSessionToken().getToken());
            if(tokenMatch) {
                oldNote.setNoteName(request.getNoteTitle());
                oldNote.setNoteDescription(request.getNoteDescription());

                noteRepository.save(oldNote);
                String json = new JSONObject()
                        .put("status", "success")
                        .put("message", "Note has been updated successfully.")
                        .toString();
                return json;
            }else{
                String json = new JSONObject()
                        .put("status", "failure")
                        .put("errorMessage", "Your session has expired.Please logout and re-login..")
                        .toString();
                return json;
            }
        }else if(request.getNoteTitle().equals(oldNote.getNoteName())
                && request.getNoteDescription().equals(oldNote.getNoteDescription())) {
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Nothing to save.")
                    .toString();
            return json;

        }else{
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "Note name and description combination must be unique.")
                    .toString();
            return json;
        }
        }

    public String getNoteContent(NoteRequest request) {
        boolean exists = noteRepository.existsById(request.getNoteID());
        if(!exists){
            String json = new JSONObject()
                    .put("status", "failure")
                    .put("errorMessage", "No such note content found.")
                    .toString();
            return json;
        }
        NoteCategory category = noteCategoryRepository
                .findNoteCategoryByNotes_NoteID(request.getNoteID());
        boolean tokenMatch = request.getToken().equals(category.getUser().getSessionToken().getToken());
        if(tokenMatch) {
            Note note = noteRepository.findNoteByNoteID(request.getNoteID());
            String noteContent = note.getNoteTextContent();

            String json = new JSONObject()
                    .put("status", "success")
                    .put("noteContent", noteContent)
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

    public String updateNoteContent(NoteRequest request) {
        //String textWithoutQuotes = request.getNoteContent().replace("\"","\'");
        NoteCategory noteCategory = noteCategoryRepository
                .findNoteCategoryByNotes_NoteID(request.getNoteID());
        boolean tokenMatch = request.getToken()
                .equals(noteCategory.getUser().getSessionToken().getToken());
        if(tokenMatch) {
            Note note = noteRepository.findNoteByNoteID(request.getNoteID());
            note.setNoteTextContent(request.getNoteContent());

            noteRepository.save(note);
            String json = new JSONObject()
                    .put("status", "success")
                    .put("message", "Note content has been saved successfully.")
                    .put("noteContent" , request.getNoteContent())
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
