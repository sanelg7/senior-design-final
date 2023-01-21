package com.seniordesign.v02.note;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seniordesign.v02.noteCategory.NoteCategory;
import com.seniordesign.v02.noteCategory.NoteCategoryRepository;
import com.seniordesign.v02.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name= "application_notes")
public class Note {

    @Id
    @SequenceGenerator(
            name = "note_sequence",
            sequenceName = "note_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "note_sequence"
    )
    @Column(name = "note_id")
    private long noteID;
    private String noteName;

    @Column(columnDefinition = "TEXT")
    private String noteTextContent;

    @JsonIgnoreProperties("notes")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "note_category_id")
    private NoteCategory noteCategory;


    private String noteDescription;


    public Note(long noteID,
                String noteName,
                String noteTextContent,
                NoteCategory noteCategory,
                User user) {
        this.noteID = noteID;
        this.noteName = noteName;
        this.noteTextContent = noteTextContent;
        this.noteCategory = noteCategory;

    }



    public Note(String noteName,
                String noteTextContent) {
        this.noteName = noteName;
        this.noteTextContent = noteTextContent;

    }
    public Note (String name,String content, NoteCategory category, String noteDescription){
        this.noteName=name;
        this.noteTextContent=content;
        this.noteCategory=category;
        this.noteDescription=noteDescription;
    }




    @Override
    public String toString() {
        return "Note{" +
                "noteID=" + noteID +
                ", noteName='" + noteName + '\'' +
                ", noteDescription='" + noteDescription + '\'' +
                '}';
    }


}
