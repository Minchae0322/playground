package com.example.playgroundmanage.location.repository;

import com.example.playgroundmanage.althlectis.dto.GameTimeDto;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.location.vo.Playground;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;

public interface PlaygroundRepositoryCustom {

    List<GameTimeDto> getPlaygroundTimeTable(Long playgroundId);
}
