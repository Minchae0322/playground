package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Teaming;
import com.example.playgroundmanage.vo.User;

import java.util.List;

public interface TeamRepositoryCustom {
    List<User> getMembers(List<Teaming> teamings);
}
