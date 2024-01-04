package com.example.playgroundmanage.config;

import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.game.vo.GameRequest;
import com.example.playgroundmanage.game.vo.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {


    private final GameRequestRepository gameRequestRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (targetType.equals("requestAccept")) {
            User user = (User) authentication.getPrincipal();
            GameRequest gameRequest = gameRequestRepository.findById((Long) targetId)
                    .orElseThrow();
            return Objects.equals(user.getId(), gameRequest.getHost().getId());
        }



        return false;
    }
}
