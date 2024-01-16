package com.example.playgroundmanage.game.vo;


import com.example.playgroundmanage.dto.UsersGameDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.example.playgroundmanage.util.DateFormat.dateFormatYYYYMMDDHHMM;

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
                .gameId(id)
                .localDateStartTime(game.getGameStartDateTime())
                .gameName(game.getGameName())
                .build();
    }

}
