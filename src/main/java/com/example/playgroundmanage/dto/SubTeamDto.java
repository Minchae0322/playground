package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.dto.response.UserInfoDto;
import com.example.playgroundmanage.store.FileService;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.impl.FileHandlerImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static com.example.playgroundmanage.store.impl.FileHandlerImpl.multipartFileToString;

@Getter
@RequiredArgsConstructor
public class SubTeamDto {

    private Long teamId;

    private String teamName;

    private String teamProfileImg;

    private List<UserInfoDto> users;

    @Builder
    public SubTeamDto(Long teamId, String teamName, InMemoryMultipartFile teamProfileImg, List<UserInfoDto> users) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamProfileImg = multipartFileToString(teamProfileImg);
        this.users = users;
    }
}
