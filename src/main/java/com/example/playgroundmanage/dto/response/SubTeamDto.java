package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SubTeamDto {

    private Long teamId;

    private String teamName;

    private MultipartFile teamImg;

    private List<UserProfileDto> users;

    @Builder
    public SubTeamDto(Long teamId, String teamName, MultipartFile teamImg, List<UserProfileDto> users) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamImg = teamImg;
        this.users = users;
    }
}

