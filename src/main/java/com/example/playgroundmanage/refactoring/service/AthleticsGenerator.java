package com.example.playgroundmanage.refactoring.service;

import com.example.playgroundmanage.refactoring.GameGenerationRequest;

public interface AthleticsGenerator {

    void generate(final Long hostId, final GameGenerationRequest gameGenerationRequest);

}
