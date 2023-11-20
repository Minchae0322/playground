package com.example.playgroundmanage.repository;


import com.example.playgroundmanage.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndProvider(String username, String provider);

    Optional<User> findByUserIdAndProvider(String username, String provider);

    boolean existsByUsername(String username);

    boolean existsByUserIdAndProvider(String userId, String provider);
}
