package com.example.playgroundmanage.location.repository.impl;

import com.example.playgroundmanage.althlectis.dto.GameTimeDto;
import com.example.playgroundmanage.althlectis.vo.QAthletics;
import com.example.playgroundmanage.location.repository.PlaygroundRepositoryCustom;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.location.vo.QPlayground;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PlaygroundRepositoryCustomImpl implements PlaygroundRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GameTimeDto> getPlaygroundTimeTable(Long playgroundId) {
        QAthletics athletics = QAthletics.athletics;
        QPlayground playground = QPlayground.playground;

        return jpaQueryFactory.select(Projections.constructor(GameTimeDto.class,
                        athletics.gameStartDateTime,
                        athletics.runningTime))
                .from(playground)
                .innerJoin(athletics)
                .on(playground.id.eq(athletics.playground.id))
                .fetch();
    }

}
