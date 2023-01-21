package com.seniordesign.v02.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seniordesign.v02.noteCategory.NoteCategory;
import com.seniordesign.v02.activity.Activity;
import com.seniordesign.v02.password.Password;
import com.seniordesign.v02.passwordCategory.PasswordCategory;
import com.seniordesign.v02.register.token.SessionToken;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_users")
@AllArgsConstructor
@Setter
@Getter
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "user_id")
    private long userID;

    private String name;
    private String lastName;
    private String secondName;
    private String email;
    private String password;
    private Boolean locked = false;

    private Boolean enabled = false;

    private String secretQuestion;
    private String secretQuestionAnswer;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    //One to many relations for other tables.
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<NoteCategory> noteCategories = new HashSet<>();

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Activity> activities = new HashSet<>();

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PasswordCategory> passwordCategories = new HashSet<>();


    //For login ops.
    @JsonIgnoreProperties("user")
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private SessionToken sessionToken;

    public User(long userID, String name, String password, String email) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(String name, String password, String email,  Set<NoteCategory> noteCategories, Set<Activity> activities, Set<PasswordCategory> passwordCategories) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.noteCategories = noteCategories;
        this.activities = activities;
        this.passwordCategories = passwordCategories;
    }
    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    // For registration
    public User(String name,String lastName, String secondName, String email, String password, String secretQuestion, String secretQuestionAnswer,UserRole userRole) {
        this.name = name;
        this.lastName = lastName;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
        this.secretQuestion = secretQuestion;
        this.secretQuestionAnswer = secretQuestionAnswer;
        this.userRole = userRole;



    }

    public User() {
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountLocked(){
        return !locked;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", locked=" + locked +
                ", enabled=" + enabled +
                ", secretQuestion='" + secretQuestion + '\'' +
                ", secretQuestionAnswer='" + secretQuestionAnswer + '\'' +
                // ", noteCategories=" + noteCategories +
                ", activities=" + activities +
               // ", passwordCategories=" + passwordCategories +
                '}';
    }
}
