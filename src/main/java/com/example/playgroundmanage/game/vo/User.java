package com.example.playgroundmanage.game.vo;



import com.example.playgroundmanage.login.dto.UserEdit;
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

    private boolean isEnable;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GameRequest> gameRequests_host;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Game> hostGames = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Teaming> teams = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GameParticipant> gameParticipants = new ArrayList<>();

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
        this.teams = teams;
        this.gameParticipants = gameParticipants;
    }



    @Transactional
    public User update(UserEdit userEdit) {
        if (userEdit.getUserNickname().equals("") || userEdit.getUserNickname().length() > 9) {
            throw new RuntimeException("닉네임 형식에 안맞습니다. 최대 글자 9자.");
        }
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
