package com.example.playgroundmanage.vo;



import com.example.playgroundmanage.dto.UserEdit;
import com.example.playgroundmanage.type.UserRole;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.io.Serializable;


@Entity
@Getter
@RequiredArgsConstructor
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userId;
    private String provider;

    private String username;

    private String email;

    private String password;

    private String phoneNumber;

    private String nickname;

    private boolean isEnable;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(String userId, Long id, String email, String username, String password, String phoneNumber, String provider, String nickname, boolean isEnable, UserRole role) {
        this.userId = userId;
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
