package com.seniordesign.v02.note;

import com.seniordesign.v02.noteCategory.NoteCategory;
import com.seniordesign.v02.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NoteRepository
            extends JpaRepository<Note,Long> {
    Optional<Note> findNoteByNoteName(String noteName);
    //naming convention is op here...
    Note findNoteByNoteID(Long noteId);
    List<Note> findNoteByNoteCategory(NoteCategory noteCategory);



}

