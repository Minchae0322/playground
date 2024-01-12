package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.Test;
import com.example.playgroundmanage.TestRepository;
import com.example.playgroundmanage.dto.UserNicknameDto;
import com.example.playgroundmanage.dto.response.TeamInfoResponse;
import com.example.playgroundmanage.dto.response.UserInfoDto;
import com.example.playgroundmanage.game.service.UserService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final TestRepository testRepository;

    @GetMapping("/user/profile")
    public UserInfoDto getUserProfile(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.getUserProfile(userDetails.getUser());
    }

    @PostMapping(value = "/user/profile-img/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateProfileImg(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        userService.updateProfileImg(myUserDetails.getUser().getId(), multipartFile);
    }


    @GetMapping("/user/info")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal MyUserDetails userDetails) {
        testRepository.save(Test.builder()
                .zonedDateTime(ZonedDateTime.now())
                .build());
        return userService.getUserInfo(userDetails.getUser());
    }

    @GetMapping("/token/valid")
    public ResponseEntity<String> validToken() {
        return ResponseEntity.ok("valid");
    }


    @GetMapping("/user/teams")
    public List<TeamInfoResponse> getTeamsUserBelongTo(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.getTeamsInfoUserBelongsTo(userDetails.getUser().getId());
    }

    @PatchMapping("/user/change-nickname")
    public UserNicknameDto changeNickname(@RequestBody UserNicknameDto userNicknameDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.changeNickname(userDetails.getUser().getId(), userNicknameDto.getUserNickname());
    }
}
