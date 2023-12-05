package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Teaming;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.vo.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<User> getMembers(List<Teaming> teamMemberRelations) {
        return teamMemberRelations.stream()
                .map(t -> jpaQueryFactory.selectFrom(QUser.user)
                        .leftJoin(QTeaming.teaming)
                        .where(QUser.user.id.eq(t.getId()))
                        .fetch().stream().findFirst().orElseThrow())
                .toList();
    }

    public List<Teaming> getTeaming(Long teamId) {
        return jpaQueryFactory.selectFrom(QTeaming.teaming)
                .leftJoin(QTeam.team)
                .where(QTeam.team.id.eq(teamId))
                .fetch();
    }
}
