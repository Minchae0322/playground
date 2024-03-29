package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.Test;
import com.example.playgroundmanage.TestRepository;
import com.example.playgroundmanage.login.dto.*;
import com.example.playgroundmanage.team.dto.TeamDto;
import com.example.playgroundmanage.login.service.UserService;
import com.example.playgroundmanage.login.service.UserDetailsServiceImpl;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.store.FileHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserDetailsServiceImpl userDetailsService;

    private final FileHandler fileHandler;

    @GetMapping("/user/info")
    public UserResponseDto getUserProfile(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.getUserInfo(userDetails.getUser());
    }

    @PostMapping(value = "/user/profile-img/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateProfileImg(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        userService.updateProfileImg(myUserDetails.getUser().getId(), multipartFile);
    }


    @PostMapping(value = "/img/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateImg(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        fileHandler.storeFile(multipartFile);
    }

    @GetMapping("/user/logout")
    @Transactional
    public ResponseEntity<Boolean> logout(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        userDetailsService.logout(myUserDetails.getUser());

        return ResponseEntity.ok(true);
    }

    @GetMapping("/token/valid")
    public ResponseEntity<String> validToken() {
        return ResponseEntity.ok("valid");
    }


    @GetMapping("/user/teams")
    public List<TeamDto.TeamResponseDto> getTeamsUserBelongTo(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.getTeamsInfoUserBelongsTo(userDetails.getUser().getId());
    }

    @PatchMapping("/user/change-nickname")
    public UserNicknameDto changeNickname(@RequestBody UserNicknameDto userNicknameDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.changeNickname(userDetails.getUser().getId(), userNicknameDto.getUserNickname());
    }

    @PostMapping("/user/signup")
    public void signup(@RequestBody UserSignupForm userSignupForm) {
        userService.signup(userSignupForm);
    }


    @GetMapping("/user/record/{userId}")
    public UserRecordResponse getUserRecord(@PathVariable Long userId) {
        return userService.getUserRecord(userId);
    }

    @GetMapping("/user/record/update")
    public UserRecordResponse updateUserRecord(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return userService.updateUserGameRecord(myUserDetails.getUser());
    }
}
