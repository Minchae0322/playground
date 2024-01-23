package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.impl.FileHandlerImpl;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static com.example.playgroundmanage.store.impl.FileHandlerImpl.multipartFileToString;

@Data
public class PendingTeamRequest {
    private Long requestId;

    private String introduction;

    private String teamName;

    private String userName;

    private String userProfileImg;
    private Long userId;

    private String requestTime;

    @Builder
    public PendingTeamRequest(Long requestId, String introduction, String teamName, String userName, Long userId, String requestTime, InMemoryMultipartFile userProfileImg) {
        this.requestId = requestId;
        this.introduction = introduction;
        this.teamName = teamName;
        this.userName = userName;
        this.userId = userId;
        this.requestTime = requestTime;
        this.userProfileImg = multipartFileToString(userProfileImg);
    }
}
