package com.example.playgroundmanage.login.repository;

import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.login.vo.UserAthleticsRecord;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserAthleticsRecordRepository extends JpaRepository<UserAthleticsRecord, Long> {
    Optional<UserAthleticsRecord> findByUser(User user);
}
