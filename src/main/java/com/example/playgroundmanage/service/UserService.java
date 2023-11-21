package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.UserSignupForm;
import com.example.playgroundmanage.exception.ExistUserException;
import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.playgroundmanage.validator.UserValidator.validateUser;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

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

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
}
