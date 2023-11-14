package com.example.playgroundmanage.vo;



import com.example.playgroundmanage.type.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;


@Entity
@Table(name = "USER")
@Getter
@Builder
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


}
