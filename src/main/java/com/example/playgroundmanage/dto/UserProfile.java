package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.vo.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfile {
    private String username; // 사용자 이름 또는 아이디

    private String provider; // 로그인한 서비스

    @Builder
    public UserProfile(String username, String provider) {
        this.username = username;
        this.provider = provider;
    }

    // DTO 파일을 통하여 Entity를 생성하는 메소드
    public User toEntity() {
        //todo password 설정까지 왜냐면 비밀번호가 설정 안되있는채로 일반로그인 하면 아이디만 치고 로그인이 가능하다.
        return User.builder()
                .username(this.username)
                .provider(this.provider)
                .build();
    }
}
