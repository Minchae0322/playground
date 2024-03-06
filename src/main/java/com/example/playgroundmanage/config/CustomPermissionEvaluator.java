package com.example.playgroundmanage.config;

import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.game.repository.TeamRequestRepository;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.team.vo.Team;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {


    private final GameRequestRepository gameRequestRepository;

    private final TeamRequestRepository teamRequestRepository;

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

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

        if (targetType.equals("delete_game")) {
            User user = (User) authentication.getPrincipal();
            Game game = gameRepository.findById((Long) targetId).orElseThrow(GameNotExistException::new);
            return Objects.equals(user.getId(), game.getHost().getId());
        }

        if (targetType.equals("delete_team")) {
            User user = (User) authentication.getPrincipal();
            Team team = teamRepository.findById((Long) targetId).orElseThrow(TeamNotExistException::new);
            return Objects.equals(user.getId(), team.getLeader().getId());
        }



        return false;
    }
}
