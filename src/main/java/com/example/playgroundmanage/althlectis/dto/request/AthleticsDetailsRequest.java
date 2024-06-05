package com.example.playgroundmanage.althlectis.dto.request;

import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.validation.constraints.NotBlank;

public record AthleticsDetailsRequest(
        @NotBlank(message = "Input Playground ID")
        Long gameId,
        @NotBlank(message = "Input Game TeamSide")
        String gameTeamSide
) {
}
