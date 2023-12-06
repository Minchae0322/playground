package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.login.vo.MyUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class UserController {
    @GetMapping("/Info")
    public String getJson(@AuthenticationPrincipal MyUserDetails userDetails) {

        userDetails.getName();
        return userDetails.getUser().getUsername();
    }

    @GetMapping("/tokenInfo")
    public String redirectToNaverAuthorization(HttpServletResponse response) {
        // 리디렉션할 URL을 반환
        response.getHeader("Authorization");
        return "http://localhost:5173/login";
    }

    @GetMapping("/oauth2/redirect")
    public String handleNaverCallback() {
        // 인가 코드 및 상태 정보 처리...

        // URL 파라미터로 정보를 붙여서 리다이렉트

        return "redirect:" ;
    }

    @GetMapping("/callback")
    public void getCode(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8080/oauth2/authorization/naver");
    }
}
