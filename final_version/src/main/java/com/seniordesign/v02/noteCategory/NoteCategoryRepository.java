package com.seniordesign.v02.noteCategory;

import com.seniordesign.v02.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteCategoryRepository extends JpaRepository<NoteCategory,Long> {
    Optional<NoteCategory> findNoteCategoryByCategoryName(String categoryName);
    List<NoteCategory> findNoteCategoryByUser_UserID(Long userID);
    List<NoteCategory> findNoteCategoryByUser_Email(String email);

    NoteCategory findNoteCategoryByNoteCategoryID(Long noteCategoryId);
    NoteCategory findNoteCategoryByNotes_NoteID(Long noteId);
}
