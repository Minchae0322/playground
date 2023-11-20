package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.vo.MyOauth2UserDetails;
import com.example.playgroundmanage.vo.MyUserDetails;
import com.example.playgroundmanage.vo.User;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
public class UserController {
    @GetMapping("/loginInfo")
    public String getJson(@AuthenticationPrincipal MyUserDetails userDetails) {

        userDetails.getName();
        return userDetails.getUser().getUsername();
    }

    @GetMapping("/auth/login/oauth2/code/n2aver")
    public String getCode(@RequestParam("code") String code, @RequestParam("state") String state) {
        return code + state;
    }
}
