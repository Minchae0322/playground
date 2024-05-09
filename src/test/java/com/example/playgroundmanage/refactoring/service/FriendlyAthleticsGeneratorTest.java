package com.example.playgroundmanage.refactoring.service;

import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.refactoring.GameGenerationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FriendlyAthleticsGeneratorTest {

    @MockBean
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private FriendlyAthleticsGenerator friendlyAthleticsGenerator;

    @Test
    void generate() {
        //given
        GameGenerationRequest gameGenerationRequest = new GameGenerationRequest(1L, )
    }
}