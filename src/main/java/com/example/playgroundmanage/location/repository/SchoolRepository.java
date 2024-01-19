package com.example.playgroundmanage.location.repository;

import com.example.playgroundmanage.location.vo.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
