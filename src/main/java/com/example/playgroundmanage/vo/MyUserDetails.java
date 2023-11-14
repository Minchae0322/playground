package com.example.playgroundmanage.vo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class MyUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return user.isEnable();
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
}
