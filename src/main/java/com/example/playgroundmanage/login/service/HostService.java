package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HostService {

    private final UserRepository userRepository;

    public User getGameHost(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);
    }
}
