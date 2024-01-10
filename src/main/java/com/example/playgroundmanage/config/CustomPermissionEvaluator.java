package com.example.playgroundmanage.config;

import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.game.repository.TeamRequestRepository;
import com.example.playgroundmanage.game.vo.GameRequest;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.TeamRequest;
import com.example.playgroundmanage.game.vo.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {


    private final GameRequestRepository gameRequestRepository;

    private final TeamRequestRepository teamRequestRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (targetType.equals("requestAccept_game")) {
            User user = (User) authentication.getPrincipal();
            GameRequest gameRequest = gameRequestRepository.findById((Long) targetId)
                    .orElseThrow();
            return Objects.equals(user.getId(), gameRequest.getHost().getId());
        }

        if (targetType.equals("requestAccept_team")) {
            User user = (User) authentication.getPrincipal();
            TeamRequest teamRequest = teamRequestRepository.findById((Long) targetId)
                    .orElseThrow();
            return Objects.equals(user.getId(), teamRequest.getLeader().getId());
        }



        return false;
    }
}
