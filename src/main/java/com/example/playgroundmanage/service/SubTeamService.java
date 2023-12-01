package com.example.playgroundmanage.service;

import com.example.playgroundmanage.repository.SubTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubTeamService {

    private final SubTeamRepository subTeamRepository;

}
