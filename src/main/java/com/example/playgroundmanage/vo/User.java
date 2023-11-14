package com.example.playgroundmanage.vo;



import com.example.playgroundmanage.exception.FormatException;
import com.example.playgroundmanage.type.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

import static com.example.playgroundmanage.validator.UserValidator.USER_NAME_FORMAT;
import static com.example.playgroundmanage.validator.UserValidator.USER_PASSWORD_FORMAT;


@Entity
@Table(name = "USER")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable{
    @Id
    @GeneratedValue
    private Long userId;
    private String username;
    private String password;
    private String phoneNumber;
    private String nickname;
    private boolean isEnable;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder
    public User(Long userId, String username, String password, String phoneNumber, String nickname, boolean isEnable, UserRole role) {
        validate(username, password);
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.isEnable = isEnable;
        this.role = role;
    }

    private void validate(String username, String password) {
        if(!username.matches(USER_NAME_FORMAT)) {
            throw new FormatException();
        }
        if(!password.matches(USER_PASSWORD_FORMAT)) {
            throw new FormatException();
        }
    }
}
