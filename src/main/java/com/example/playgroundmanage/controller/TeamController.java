package com.example.playgroundmanage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {

    @GetMapping("/team/{teamId}/info")
    public void getTeamInfo(@PathVariable Long teamId) {

    }
}
