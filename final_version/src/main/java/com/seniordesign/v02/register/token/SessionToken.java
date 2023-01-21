package com.seniordesign.v02.register.token;

import com.seniordesign.v02.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@Entity
public class SessionToken {

    @Id
    @Column(name = "user_id")
    private Long id;
    private String token;


    @OneToOne
    @MapsId
    @JoinColumn(nullable = false,name = "user_id")
    private User user;


    public SessionToken(String token, User user) {
        this.token = token;
        this.user = user;
    }
}

