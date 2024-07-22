package com.example.playgroundmanage.althlectis.service;

import com.example.playgroundmanage.althlectis.dto.request.GameGenerationRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AthleticsGenerator {

    String getType();


    Long generate(final Long hostId, final GameGenerationRequest gameGenerationRequest);

}
