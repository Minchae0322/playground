package com.example.playgroundmanage.game.vo;


import com.example.playgroundmanage.dto.UsersGameDto;
import com.example.playgroundmanage.dto.response.UserInfoDto;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.example.playgroundmanage.util.DateFormat.dateFormatYYYYMMDDHHMM;
import static com.example.playgroundmanage.util.LocationFormatter.getLocation;

@Entity
@Getter
@RequiredArgsConstructor
public class GameParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    private SubTeam subTeam;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Game game;

    private boolean isAccepted;

    @Builder
    public GameParticipant(Long id, User user, SubTeam subTeam, Game game, boolean isAccepted) {
        this.id = id;
        this.user = user;
        this.subTeam = subTeam;
        this.game = game;
        this.isAccepted = isAccepted;
    }

    public UsersGameDto.UsersGameResponseDto toUsersGameResponseDto() {
        return UsersGameDto.UsersGameResponseDto.builder()
                .gameStart(dateFormatYYYYMMDDHHMM(game.getGameStartDateTime()))
                .hostName(game.getHost().getNickname())
                .runningTime(game.getRunningTime())
                .gameId(game.getId())
                .playgroundId(game.getPlayground().getId())
                .location(getLocation(game))
                .subTeamName(subTeam == null ? "" : subTeam.getTeamName())
                .localDateStartTime(game.getGameStartDateTime())
                .gameName(game.getGameName())
                .build();
    }

    public UserInfoDto toUserInfoDto(InMemoryMultipartFile inMemoryMultipartFile) {
        return UserInfoDto.builder()
                .userNickname(user.getNickname())
                .userId(id)
                .userProfileImg(inMemoryMultipartFile)
                .build();
    }

}
