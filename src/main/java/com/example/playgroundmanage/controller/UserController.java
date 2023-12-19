package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.response.TeamInfoResponse;
import com.example.playgroundmanage.dto.response.UserProfileDto;
import com.example.playgroundmanage.game.service.UserService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/profile")
    public UserProfileDto getUserProfile(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.getUserProfile(userDetails.getUser());
    }




    @GetMapping("/token/valid")
    public ResponseEntity<String> validToken() {
        return ResponseEntity.ok("valid");
    }


    @GetMapping("/user/teams")
    public List<TeamInfoResponse> getTeamsUserBelongTo(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.getTeamsUserBelongsTo(userDetails.getUser().getId()).stream()
                .map(t -> TeamInfoResponse.builder()
                        .teamName(t.getTeamName())
                        .teamId(t.getId())
                        .build()).toList();
    }

}
