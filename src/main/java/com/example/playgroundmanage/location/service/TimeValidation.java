package com.example.playgroundmanage.location.service;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Predicate;

@Component
public class TimeValidation {
    public boolean isAfterFromNow(LocalDateTime gameStartDateTime) {
        return gameStartDateTime.isAfter(LocalDateTime.now());
    }
}
