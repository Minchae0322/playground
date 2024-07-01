package com.example.playgroundmanage.althlectis.repo;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.login.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AthleticsRepository extends JpaRepository<Athletics, Long> {

    List<Athletics> findAllByHost(User host);
}
