package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;

import com.example.playgroundmanage.login.dto.UserGameInfoResponse;
import com.example.playgroundmanage.login.vo.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHostGameService {

    private final AthleticsRepository athleticsRepository;

    @Transactional
    public List<UserGameInfoResponse> getUserHostGames(User user) {
        List<Athletics> athletics = athleticsRepository.findAllByHost(user);

        return athletics.stream()
                .sorted()
                .map(UserGameInfoResponse::of)
                .toList();
    }

    @Transactional
    public List<UserGameInfoResponse> getUserHostGamesInMonth(User user, LocalDateTime localDateTime) {
        List<Athletics> athletics = athleticsRepository.findAllByHost(user);


        return athletics.stream()
                .filter(athletics1 -> isGameOnYearMonth(athletics1, localDateTime))
                .sorted()
                .map(UserGameInfoResponse::of)
                .toList();
    }

    private boolean isGameOnYearMonth(Athletics athletics, LocalDateTime localDateTime) {
        int targetYear = localDateTime.getYear();
        int targetMonth = localDateTime.getMonthValue();

        return athletics.getGameStartDateTime().getYear() == targetYear && athletics.getGameStartDateTime().getMonthValue() == targetMonth;
    }


}
