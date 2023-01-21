package com.seniordesign.v02.register.token;

import com.seniordesign.v02.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    @Column(nullable = false)
    private Long id;
    private String token;


    @Column(nullable = false)
    private LocalDateTime createTime;
    @Column(nullable = false)
    private LocalDateTime expirationTime;

    private LocalDateTime verificationTime;

    @ManyToOne
    @JoinColumn(nullable = false,name = "user_id")
    private User user;


    public ConfirmationToken(String token, LocalDateTime createTime, LocalDateTime expirationTime,User user) {
        this.token = token;
        this.createTime = createTime;
        this.expirationTime = expirationTime;
        this.user = user;
    }
}
