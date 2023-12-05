package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Teaming;
import com.example.playgroundmanage.game.vo.User;

import java.util.List;

public interface TeamRepositoryCustom {
    List<User> getMembers(List<Teaming> teamings);
}
