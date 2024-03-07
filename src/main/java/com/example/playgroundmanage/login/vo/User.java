package com.example.playgroundmanage.login.vo;



import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.login.dto.UserEdit;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.store.UploadFile;
import com.example.playgroundmanage.team.vo.Teaming;
import com.example.playgroundmanage.type.UserRole;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    @OneToOne
    private UploadFile profileImg;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserGameRecord userGameRecord;

    private boolean isEnable;

    private boolean isLoggedIn;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GameRequest> gameRequests_host;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Game> hostGames;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Teaming> teams;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<GameParticipant> gameParticipants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GameRequest> gameRequests;

    @Builder
    public User(Long id, String username, String provider, String email, String password, String phoneNumber, String nickname, boolean isEnable, UserRole role, List<Teaming> teams, List<GameParticipant> gameParticipants) {
        this.id = id;
        this.username = username;
        this.provider = provider;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.isEnable = isEnable;
        this.role = role;
        this.hostGames = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.gameRequests = new ArrayList<>();
        this.gameRequests_host = new ArrayList<>();
        this.gameParticipants = new ArrayList<>();
    }



    public void enable() {
        isLoggedIn = true;
    }

    public void disable() {
        isLoggedIn = false;
    }

    private void validate(String nickname) {
        if (nickname.equals("") || nickname.length() > 12) {
            throw new RuntimeException("不符合昵称格式。 最大字数9个字");
        }

    }

    @Transactional
    public User update(UserEdit userEdit) {
        validate(userEdit.getUserNickname());
        this.nickname = userEdit.getUserNickname();
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User) {
            return Objects.equals(((User) obj).getId(), this.id);
        }
        return false;
    }

    @Transactional
    public User updateProfile(UploadFile uploadFile) {
        this.profileImg = uploadFile;
        return this;
    }
}
