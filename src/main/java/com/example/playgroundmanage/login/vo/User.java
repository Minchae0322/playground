package com.example.playgroundmanage.login.vo;



import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.login.dto.UserEdit;
import com.example.playgroundmanage.store.vo.UploadFile;
import com.example.playgroundmanage.team.vo.Teaming;
import com.example.playgroundmanage.type.UserRole;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nickname;

    private String provider;

    private String password;

    private boolean isEnable;

    private boolean isLoggedIn;

    @Enumerated
    private UserRole role;

    @OneToOne
    private UploadFile userProfileImg;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserGameRecord userGameRecord;


    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Game> hostGames;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AthleticsParticipant> athleticsParticipants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Teaming> teams;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<GameParticipant> gameParticipants;




    @Builder
    public User(Long id, String username, String nickname, String provider, String password, boolean isEnable, boolean isLoggedIn, UserRole role, UploadFile userProfileImg, UserGameRecord userGameRecord, List<Teaming> teams, List<GameParticipant> gameParticipants) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.provider = provider;
        this.password = password;
        this.isEnable = isEnable;
        this.isLoggedIn = isLoggedIn;
        this.role = role;
        this.userProfileImg = userProfileImg;
        this.userGameRecord = userGameRecord;
        this.hostGames = new ArrayList<>();
        this.teams = teams;
        this.gameParticipants = new ArrayList<>();
        this.athleticsParticipants = new ArrayList<>();
    }


    public UploadFile getUserProfileImg() {
        if (userProfileImg == null) {
            return UploadFile.builder()
                    .storeFileName("")
                    .orgFileName("")
                    .build(); // null일 때 기본 객체 반환
        }
        return userProfileImg;
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
        this.userProfileImg = uploadFile;
        return this;
    }
}
