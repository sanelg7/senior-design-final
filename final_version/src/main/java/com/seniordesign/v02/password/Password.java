package com.seniordesign.v02.password;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seniordesign.v02.noteCategory.NoteCategory;
import com.seniordesign.v02.passwordCategory.PasswordCategory;
import com.seniordesign.v02.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "application_passwords")
public class Password {

    @Id
    @SequenceGenerator(
            name = "password_sequence",
            sequenceName = "password_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "password_sequence"
    )
    @Column(name = "password_id")
    private long passwordID;

    private String passwordTitle;
    private String passwordDescription;

    @Column(columnDefinition = "TEXT")
    private String passwordContent;

    private boolean secure;
    private String secureKey;

    @JsonIgnoreProperties("passwords")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "password_category_id")
    private PasswordCategory passwordCategory;

    public Password(String passwordTitle,String passwordDescription, boolean secure,String secureKey) {
        this.passwordTitle = passwordTitle;
        this.passwordDescription = passwordDescription;
        this.secure = secure;
        this.secureKey = secureKey;
    }

    public Password(String passwordTitle, String passwordDescription, boolean secure) {
        this.passwordTitle = passwordTitle;
        this.passwordDescription = passwordDescription;
        this.secure = secure;
    }

    public Password(String passwordTitle, String passwordDescription, boolean secure, String encodedSecureKey, PasswordCategory passwordCategory) {
        this.passwordTitle=passwordTitle;
        this.passwordDescription=passwordDescription;
        this.secure=secure;
        this.secureKey=encodedSecureKey;
        this.passwordCategory=passwordCategory;
    }


    @Override
    public String toString() {
        return "Password{" +
                "passwordID=" + passwordID +
                ", passwordTitle='" + passwordTitle + '\'' +
                ", passwordContent='" + passwordContent + '\'' +
                ", isSecure=" + secure +
                ", passwordCategory=" + passwordCategory +
                '}';
    }
}
