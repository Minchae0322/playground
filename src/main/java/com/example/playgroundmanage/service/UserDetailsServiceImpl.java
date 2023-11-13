package com.example.playgroundmanage.service;

import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.vo.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(user -> new  MyUserDetails(user, Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())))).orElseThrow();

    }

}
