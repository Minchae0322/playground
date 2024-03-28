package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.login.dto.UserInfoDto;
import com.example.playgroundmanage.login.dto.UserResponseDto;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.playgroundmanage.store.impl.FileHandlerImpl.multipartFileToString;

@Getter
@RequiredArgsConstructor
public class SubTeamDto {

    private Long teamId;

    private String teamName;

    private String teamDescription;

    private Long subTeamId;

    private String teamProfileImg;

    private List<UserResponseDto> users;

    @Builder
    public SubTeamDto(Long teamId, String teamName, Long subTeamId,String teamDescription, InMemoryMultipartFile teamProfileImg, List<UserResponseDto> users) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.subTeamId = subTeamId;
        this.teamDescription = teamDescription;
        this.teamProfileImg = multipartFileToString(teamProfileImg);
        this.users = users;
    }
}

