package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.store.InMemoryMultipartFile;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static com.example.playgroundmanage.store.impl.FileHandlerImpl.multipartFileToString;

@RequiredArgsConstructor
@Getter
public class UserInfoDto {
    private Long userId;
    private String userNickname;

    private String userRole;

    private String userProfileImg;

    @Builder
    public UserInfoDto(Long userId, String userNickname, String userRole, String userProfileImg)  {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userRole = userRole;
        this.userProfileImg = userProfileImg;
    }

    @Builder
    public UserInfoDto(Long userId, String userNickname, String userRole, InMemoryMultipartFile userProfileImg) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userRole = userRole;

    }
}
