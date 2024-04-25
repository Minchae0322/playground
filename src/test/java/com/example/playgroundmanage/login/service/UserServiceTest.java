package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.login.dto.UserDtoConverter;
import com.example.playgroundmanage.login.dto.UserRequestDto;
import com.example.playgroundmanage.login.dto.UserResponseDto;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.store.vo.UploadFile;
import com.example.playgroundmanage.team.dto.TeamDto;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.team.vo.Teaming;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDtoConverter userDtoConverter;

    private final String provider = "wechat";

    @Test
    void getUser() {
        //given
        Long userId = 1L;
        User mockUser = getMockUser(userId);
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));

        //when
        User user = userService.getUser(mockUser.getUsername());

        //then
        assertEquals(userId, user.getId());
        assertEquals(provider, user.getProvider());
    }


    @Test
    void getTeamsInfoUserBelongsTo() {
        //given
        Long userId = 1L;

        Teaming mockTeam1 = Teaming.builder()
                .team(getMockTeam("mockTeam1"))
                .build();

        Teaming mockTeam2 = Teaming.builder()
                .team(getMockTeam("mockTeam2"))
                .build();

        User mockUser = User.builder()
                .id(userId)
                .role(UserRole.USER)
                .provider(provider)
                .username("user1")
                .isEnable(true)
                .teams(new ArrayList<>(List.of(mockTeam1, mockTeam2)))
                .isLoggedIn(true)
                .nickname("user1")
                .build();

        //when
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        List<TeamDto.TeamResponseDto> results = userService.getTeamsInfoUserBelongsTo(userId);

        //then
        assertEquals(2, results.size());
        assertEquals("mockTeam1", results.get(0).getTeamName());
    }

    @Test
    void isValidUserNickname() {
        //given
        String nickName = "user1";
        String validNickname = "name";
        //when

        when(userRepository.existsByNickname(any())).thenReturn(false);
        when(userRepository.existsByNickname(nickName)).thenReturn(true);

        boolean result = userService.isValidUserNickname(nickName);
        boolean result2 = userService.isValidUserNickname(validNickname);

        //then
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    void changeNickname() {
        //given
        String validNickname = "myName";
        UserRequestDto requestDto = UserRequestDto.builder()
                .userId(1L)
                .userNickname(validNickname)
                .build();
        User mockUser = getMockUser(1L);

        //when
        when(userService.isValidUserNickname(validNickname)).thenReturn(false);
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userDtoConverter.toUserNicknameResponseDto(requestDto.getUserNickname())).thenReturn(UserResponseDto.builder()
                .userNickname(validNickname)
                .build());

        // when
        UserResponseDto responseDto = userService.changeNickname(requestDto);

        // then
        assertEquals("myName", responseDto.getUserNickname());
    }


    private User getMockUser(Long userId) {
        return User.builder()
                .id(userId)
                .role(UserRole.USER)
                .provider(provider)
                .username("user1")
                .isEnable(true)
                .isLoggedIn(true)
                .nickname("user1")
                .build();
    }

    private Team getMockTeam(String teamName) {
        return Team.builder()
                .teamName(teamName)
                .description("")
                .teamProfileImg(UploadFile.builder()
                        .orgFileName("")
                        .storeFileName("")
                        .build())
                .sportsEvent(SportsEvent.SOCCER)
                .build();
    }
}