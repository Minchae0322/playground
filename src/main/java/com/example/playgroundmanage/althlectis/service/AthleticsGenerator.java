package com.example.playgroundmanage.althlectis.service;

import com.example.playgroundmanage.althlectis.dto.request.GameGenerationRequest;

public interface AthleticsGenerator {

    String getType();

    Long generate(final Long hostId, final GameGenerationRequest gameGenerationRequest);

}
