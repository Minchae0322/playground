package com.example.playgroundmanage.repository;


import com.example.playgroundmanage.vo.Teaming;
import com.example.playgroundmanage.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndProvider(String username, String provider);

    boolean existsByUsername(String username);

}
