package com.example.playgroundmanage.login.dto;

import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.store.FileHandler;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

    private final FileHandler fileHandler;

    // FileHandler 의존성 주입
    public UserDtoConverter(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public UserResponseDto toUserResponseDto(User user) {
        return toUserResponseDto(user, null);
    }

    public UserResponseDto toUserInTeamResponseDto(User user, String role) {
        return toUserResponseDto(user, role);
    }

    public UserResponseDto toUserNicknameResponseDto(String nickname) {
        return UserResponseDto.builder()
                .userNickname(nickname)
                .build();
    }


    private UserResponseDto toUserResponseDto(User user, String role) {
        UserResponseDto.UserResponseDtoBuilder builder = UserResponseDto.builder()
                .userId(user.getId())
                .userNickname(user.getNickname())
                .userProfileImg(user.getProfileImg().getFileUrl());

        if (role != null) {
            builder.userRole(role);
        }

        return builder.build();
    }
}
