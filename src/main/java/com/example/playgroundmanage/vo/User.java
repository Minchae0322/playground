package com.example.playgroundmanage.vo;



import com.example.playgroundmanage.dto.UserEdit;
import com.example.playgroundmanage.exception.FormatException;
import com.example.playgroundmanage.type.UserRole;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

import static com.example.playgroundmanage.validator.UserValidator.USER_NAME_FORMAT;
import static com.example.playgroundmanage.validator.UserValidator.USER_PASSWORD_FORMAT;


@Entity
@Table(name = "USER")
@Getter
@DynamicUpdate
@RequiredArgsConstructor
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private String provider;
    private String nickname;
    private boolean isEnable;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(Long userId, String email, String username, String password, String phoneNumber, String provider, String nickname, boolean isEnable, UserRole role) {
        this.userId = userId;
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
