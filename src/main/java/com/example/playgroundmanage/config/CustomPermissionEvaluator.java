package com.example.playgroundmanage.config;

import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;


import com.example.playgroundmanage.team.repository.TeamRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.team.vo.Team;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {





    private final AthleticsRepository athleticsRepository;

    private final TeamRepository teamRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        if (targetType.equals("requestAccept_team")) {
            User user = (User) authentication.getPrincipal();

            return true;//todo
        }

        if (targetType.equals("delete_game")) {
            User user = (User) authentication.getPrincipal();
            Athletics athletics = athleticsRepository.findById((Long) targetId).orElseThrow(GameNotExistException::new);
            return Objects.equals(user.getId(), athletics.getHost().getId());
        }

        if (targetType.equals("summit_game")) {
            User user = (User) authentication.getPrincipal();
            Athletics athletics = athleticsRepository.findById((Long) targetId).orElseThrow(GameNotExistException::new);
            return Objects.equals(user.getId(), athletics.getHost().getId());
        }

        if (targetType.equals("delete_team")) {
            User user = (User) authentication.getPrincipal();
            Team team = teamRepository.findById((Long) targetId).orElseThrow(TeamNotExistException::new);
            return Objects.equals(user.getId(), team.getLeader().getId());
        }



        return false;
    }
}
