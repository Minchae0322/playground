package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Game;
import com.example.playgroundmanage.vo.User;

import java.util.List;

public interface GameRepositoryCustom {
    List<Game> getHostCreatedGamesNotStarted(User user);
}
