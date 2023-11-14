package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.UserSignupForm;
import com.example.playgroundmanage.exception.ExistUserException;
import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.MyUserDetails;
import com.example.playgroundmanage.vo.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.playgroundmanage.validator.UserValidator.validateUser;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(user -> new MyUserDetails(user, Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())))).orElseThrow();
    }

    @Transactional
    public void signup(UserSignupForm userSignupForm) {
        validateUser(userSignupForm.getUsername(), userSignupForm.getPassword());
        if(userRepository.existsByUsername(userSignupForm.getUsername())) {
            throw new ExistUserException();
        }
        User user = User.builder()
                .username(userSignupForm.getUsername())
                .password(passwordEncoder.encode(userSignupForm.getPassword()))
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
    }

}
