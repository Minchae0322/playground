package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GameRepositoryCustomImpl implements GameRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Game> getHostCreatedGamesNotStarted(User user) {
        return null;
    }
}
