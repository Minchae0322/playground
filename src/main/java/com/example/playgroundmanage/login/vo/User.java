package com.example.playgroundmanage.login.vo;



import com.example.playgroundmanage.login.dto.UserEdit;
import com.example.playgroundmanage.type.UserRole;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.io.Serializable;


@Entity
@Getter
@RequiredArgsConstructor
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;
    private String provider;

    private String email;

    private String password;

    private String phoneNumber;

    private String nickname;

    private boolean isEnable;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User( Long id, String email, String username, String password, String phoneNumber, String provider, String nickname, boolean isEnable, UserRole role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.provider = provider;
        this.nickname = nickname;
        this.isEnable = isEnable;
        this.role = role;
    }

    @Transactional
    public User update(UserEdit userEdit) {
        this.username = username;
        return this;
    }
}
