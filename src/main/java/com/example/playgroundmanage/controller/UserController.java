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

    @GetMapping("/login/oauth2")
    public String redirectToNaverAuthorization() {
        // 리디렉션할 URL을 반환
        return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=LvLgBDoxDkpt12w_uWf4&scope=name&state=SNolgmAQB_nWiNpuYQ4KVUFUPZDGJvXu-97cOB8Sc8A%3D&redirect_uri=http://localhost:8080/login/oauth2/code/naver";
    }

    @GetMapping("/auth/login/oauth2/code/n2aver")
    public String getCode(@RequestParam("code") String code, @RequestParam("state") String state) {
        return code + state;
    }
}
