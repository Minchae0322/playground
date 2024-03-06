package com.example.playgroundmanage.login.repository;

import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.login.vo.UserGameRecord;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserGameRecordRepository extends JpaRepository<UserGameRecord, Long> {
    Optional<UserGameRecord> findUserGameRecordByUser(User user);
}
