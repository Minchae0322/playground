package com.example.playgroundmanage.controller;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController

public class UserController {
    @GetMapping("/loginInfo")
    public String getJson(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        return attributes.toString();
    }

    @GetMapping("/auth/login/oauth2/code/n2aver")
    public String getCode(@RequestParam("code") String code, @RequestParam("state") String state) {
        return code + state;
    }
}
