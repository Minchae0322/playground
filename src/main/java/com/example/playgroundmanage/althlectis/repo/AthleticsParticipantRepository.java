package com.example.playgroundmanage.althlectis.repo;

import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.login.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AthleticsParticipantRepository extends JpaRepository<AthleticsParticipant, Long> {

    List<AthleticsParticipant> findAllByUser(User user);


}
