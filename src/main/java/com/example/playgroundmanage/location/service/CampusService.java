package com.example.playgroundmanage.location.service;


import com.example.playgroundmanage.althlectis.dto.response.AthleticsResponse;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.exception.CampusNotExistException;
import com.example.playgroundmanage.exception.SchoolNotExistException;
import com.example.playgroundmanage.location.repository.CampusRepository;
import com.example.playgroundmanage.location.dto.CampusResponseDto;
import com.example.playgroundmanage.location.dto.response.PlaygroundInfoResponse;
import com.example.playgroundmanage.location.repository.SchoolRepository;
import com.example.playgroundmanage.location.vo.Campus;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.location.vo.School;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampusService {

    private final CampusRepository campusRepository;

    private final SchoolRepository schoolRepository;

    private final TimeValidation timeValidation;


    @Transactional
    public List<AthleticsResponse> getUpcomingGamesInCampusBySportsEvent(Long campusId, SportsEvent sportsEvent) {
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(CampusNotExistException::new);

        List<Playground> playgrounds = getPlaygroundsBySportsEvent(campus.getPlaygrounds(), sportsEvent);

        List<Athletics> athletics = playgrounds.stream()
                .flatMap(playground -> getUpcomingAthletics(playground.getAthletics()).stream())
                .sorted(Comparator.comparing(Athletics::getGameStartDateTime))
                .toList();

        return athletics.stream()
                .map(AthleticsResponse::of)
                .toList();
    }

    private List<Athletics> getUpcomingAthletics(List<Athletics> athletics) {
        return athletics.stream()
                .filter(athletics1 -> timeValidation.isAfterFromNow(athletics1.getGameStartDateTime()))
                .toList();
    }

    @Transactional
    public List<PlaygroundInfoResponse> getPlaygroundByCampusAndSportsType(Long campusId, SportsEvent valueOf) {
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(CampusNotExistException::new);

        List<Playground> playgrounds = getPlaygroundsBySportsEvent(campus.getPlaygrounds(), valueOf);

        return playgrounds.stream()
                .map(PlaygroundInfoResponse::of)
                .toList();
    }

    private List<Playground> getPlaygroundsBySportsEvent(List<Playground> playgrounds, SportsEvent sportsEvent) {
        return playgrounds.stream()
                .filter(playground -> playground.getSportsEvent().equals(sportsEvent))
                .toList();
    }



    public List<CampusResponseDto> getCampusList(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);

        return school.getCampus().stream()
                .map(campus -> CampusResponseDto.builder()
                        .campusName(campus.getCampusName())
                        .campusId(campus.getId())
                        .build())
                .toList();
    }




}
