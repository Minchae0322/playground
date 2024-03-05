package com.example.playgroundmanage.login.vo;


import com.example.playgroundmanage.game.vo.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


@Getter

public class MyUserDetails implements UserDetails, OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;

    // 표준 사용자 이름/비밀번호 인증을 위한 생성자
    public MyUserDetails(User user) {
        this.user = user;
        this.attributes =  Collections.emptyMap(); // OAuth 사용자가 아닌 경우 속성을 null로 설정
    }

    // OAuth2 인증을 위한 생성자
    public MyUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = (attributes != null) ? attributes : Collections.emptyMap();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(user.getRole().getValue()));
        return auth;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isLoggedIn();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isEnable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isEnable();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnable();
    }

    @Override
    public String getName() {
        return user.getUsername();
    }
}
