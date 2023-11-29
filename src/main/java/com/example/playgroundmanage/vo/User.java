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

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
    private List<Game> hostGames = new ArrayList<>();

    @Builder
    public User(Long id, String username, String provider, String email, String password, String phoneNumber, String nickname, boolean isEnable, UserRole role, List<Teaming> teams, List<MatchParticipant> matchParticipants, List<Game> hostGames) {
        this.id = id;
        this.username = username;
        this.provider = provider;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.isEnable = isEnable;
        this.role = role;
        this.teams = teams;
        this.matchParticipants = matchParticipants;
        this.hostGames = hostGames;
    }



    @Transactional
    public User update(UserEdit userEdit) {
        this.username = username;
        return this;
    }
}
