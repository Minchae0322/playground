package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.exception.UserNotValidException;
import com.example.playgroundmanage.login.dto.*;
import com.example.playgroundmanage.login.service.UserHostGameService;
import com.example.playgroundmanage.login.service.UserJoinGameService;
import com.example.playgroundmanage.team.dto.TeamDto;
import com.example.playgroundmanage.login.service.UserService;
import com.example.playgroundmanage.login.service.UserDetailsServiceImpl;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.date.MyDateTimeLocal.initMyDateTime;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/user/info")
    public UserResponseDto getUserInfo(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.getUserInfo(userDetails.getUser().getId());
    }

    @PostMapping(value = "/user/profile-img/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateUserProfileImg(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestParam("image") MultipartFile multipartFile) {
        userService.updateUserProfileImg(myUserDetails.getUser().getId(), multipartFile);
    }

    @GetMapping("/user/logout")
    public ResponseEntity<Boolean> logout(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        userDetailsService.logout(myUserDetails.getUser());

        return ResponseEntity.ok(true);
    }

    @GetMapping("/token/valid")
    public ResponseEntity<String> validateUserJWTToken(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        if(!myUserDetails.isEnabled()) {
            throw new UserNotValidException();
        }

        return ResponseEntity.ok("valid");
    }


    @GetMapping("/user/teams")
    public List<TeamDto.TeamResponseDto> getTeamsUserBelongTo(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userService.getTeamsInfoUserBelongsTo(userDetails.getUser().getId());
    }

    @PatchMapping("/user/change-nickname")
    public UserResponseDto changeNickname(@RequestBody UserRequestDto userRequestDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        userRequestDto.setUserId(userDetails.getUser().getId());

        return userService.changeNickname(userRequestDto);
    }

/*    @PostMapping("/user/signup")
    public void signup(@RequestBody UserSignupForm userSignupForm) {
        userService.signup(userSignupForm);
    }*/





    @GetMapping("/user/record/{userId}")
    public UserRecordResponse getUserRecord(@PathVariable Long userId) {
        return userService.getUserRecord(userId);
    }

    @GetMapping("/user/record/update")
    public UserRecordResponse updateUserRecord(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return userService.updateUserGameRecord(myUserDetails.getUser());
    }

    @GetMapping("/user/ranking")
    public List<UserRecordResponse> getUsersRanking() {
        return userService.getUsersRanking();
    }



}
