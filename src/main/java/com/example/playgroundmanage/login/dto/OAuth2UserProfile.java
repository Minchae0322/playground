package com.example.playgroundmanage.login.dto;

import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Random;

@Getter
@RequiredArgsConstructor
public class OAuth2UserProfile {
    private String username; // 사용자 이름 또는 아이디

    private String provider; // 로그인한 서비스

    private Map<String, Object> attributes;


    @Builder
    public OAuth2UserProfile(String username, String provider, Map<String, Object> attributes) {
        this.username = username;
        this.provider = provider;
        this.attributes = attributes;
    }






    // DTO 파일을 통하여 Entity를 생성하는 메소드
    public User toEntity() {
        //todo password 설정까지 왜냐면 비밀번호가 설정 안되있는채로 일반로그인 하면 아이디만 치고 로그인이 가능하다.
        Random random = new Random();
        int randomNum = random.nextInt(1000000) + 1;
        return User.builder()
                .isEnable(true)
                .role(UserRole.USER)
                .username(this.username)
                .provider(this.provider)
                .nickname("USER" + randomNum)
                .build();
    }

}
