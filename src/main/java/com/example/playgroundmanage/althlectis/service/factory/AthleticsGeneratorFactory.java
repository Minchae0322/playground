package com.example.playgroundmanage.althlectis.service.factory;

import com.example.playgroundmanage.althlectis.service.AthleticsGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AthleticsGeneratorFactory {

    private final List<AthleticsGenerator> gameGenerators;

    public AthleticsGenerator find(String type) {
        return gameGenerators.stream()
                .filter(requestService -> requestService.getType().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
