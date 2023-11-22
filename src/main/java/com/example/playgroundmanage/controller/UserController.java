package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.login.vo.MyUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @GetMapping("/Info")
    public String getJson(@AuthenticationPrincipal MyUserDetails userDetails) {

        userDetails.getName();
        return userDetails.getUser().getUsername();
    }

    @GetMapping("/auth/login/oauth2/code/n2aver")
    public String getCode(@RequestParam("code") String code, @RequestParam("state") String state) {
        return code + state;
    }
}
