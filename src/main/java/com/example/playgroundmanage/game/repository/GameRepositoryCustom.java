package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.User;

import java.util.List;

public interface GameRepositoryCustom {
    List<Game> getHostCreatedGamesNotStarted(User user);
}
