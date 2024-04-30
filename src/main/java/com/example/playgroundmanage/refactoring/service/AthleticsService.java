package com.example.playgroundmanage.refactoring.service;

import com.example.playgroundmanage.refactoring.repo.AthleticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AthleticsService {

    private final AthleticsRepository athleticsRepository;


}
