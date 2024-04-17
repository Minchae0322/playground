package com.example.playgroundmanage.location.service;

import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
import com.example.playgroundmanage.location.vo.Playground;
import org.springframework.stereotype.Component;

@Component
public class PlaygroundDtoConverter {

    public PlaygroundResponseDto toPlaygroundResponseInfoDto(Playground playground) {
        return PlaygroundResponseDto.builder()
                .playgroundId(playground.getId())
                .campusName(playground.getCampus().getCampusName())
                .playgroundName(playground.getName())
                .playgroundProfileImg(playground.getImg().getFileUrl())
                .schoolName(playground.getCampus().getSchool().getSchoolName())
                .sportsEvent(playground.getSportsEvent().getValue_cn())
                .build();
    }
}
