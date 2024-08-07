package com.example.playgroundmanage.login.repository;


import com.example.playgroundmanage.login.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndProvider(String username, String provider);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

}
