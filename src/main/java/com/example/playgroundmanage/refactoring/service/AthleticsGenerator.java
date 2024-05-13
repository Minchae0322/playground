package com.example.playgroundmanage.refactoring.service;

import com.example.playgroundmanage.refactoring.GameGenerationRequest;

public interface AthleticsGenerator {

    String getType();

    Long generate(final Long hostId, final GameGenerationRequest gameGenerationRequest);

}
