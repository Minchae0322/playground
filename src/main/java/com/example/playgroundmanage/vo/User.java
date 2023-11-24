package com.example.playgroundmanage.vo;



import com.example.playgroundmanage.login.dto.UserEdit;
import com.example.playgroundmanage.type.UserRole;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@RequiredArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Teaming> teams = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MatchParticipant> matchParticipants = new ArrayList<>();

    @Transactional
    public User update(UserEdit userEdit) {
        this.username = username;
        return this;
    }
}
