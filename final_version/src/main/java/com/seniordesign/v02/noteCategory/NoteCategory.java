package com.seniordesign.v02.noteCategory;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seniordesign.v02.note.Note;
import com.seniordesign.v02.user.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name= "application_note_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoteCategory {

    @Id
    @SequenceGenerator(
            name = "note_category_sequence",
            sequenceName = "note_category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "note_category_sequence"
    )
    @Column(name = "note_category_id")
    private long noteCategoryID;
    private String categoryName;

    @JsonIgnoreProperties("noteCategories")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;


    //One to many relations for other tables.Cascade and orphan removal to enable adding notes.
    @JsonIgnoreProperties("noteCategory")
    @OneToMany(mappedBy = "noteCategory",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<Note> notes = new HashSet<>();


    //2 args constructor
    public NoteCategory(User user,String categoryName){
        this.categoryName = categoryName;
        this.user = user;
    }

    @Override
    public String toString() {
        return "NoteCategory{" +
                "noteCategoryID=" + noteCategoryID +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
