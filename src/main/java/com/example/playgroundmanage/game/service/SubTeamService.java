package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.game.repository.SubTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubTeamService {

    private final SubTeamRepository subTeamRepository;

}
