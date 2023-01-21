package com.seniordesign.v02.passwordCategory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seniordesign.v02.password.Password;
import com.seniordesign.v02.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_password_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordCategory {

    @Id
    @SequenceGenerator(
            name = "password_category_sequence",
            sequenceName = "password_category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "password_category_sequence"
    )
    @Column(name = "passwword_category_id")
    private long passwordCategoryID;

    private String passwordCategoryName;


    @JsonIgnoreProperties("passwordCategories")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;


    @JsonIgnoreProperties("passwordCategory")
    @OneToMany(mappedBy = "passwordCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Password> passwords = new HashSet<>();

    public PasswordCategory(User user, String passwordCategoryName) {
        this.user = user;
        this.passwordCategoryName = passwordCategoryName;
    }

    @Override
    public String toString() {
        return "PasswordCategory{" +
                "passwordCategoryID=" + passwordCategoryID +
                ", passwordCategoryName='" + passwordCategoryName + '\'' +
                ", passwords=" + passwords +
                '}';
    }
}
