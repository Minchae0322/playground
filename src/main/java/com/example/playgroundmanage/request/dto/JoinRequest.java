package com.example.playgroundmanage.request.dto;

import jakarta.validation.constraints.NotBlank;

public record JoinRequest(
        @NotBlank(message = "Input Playground ID")
        Long athleticsId,

        @NotBlank(message = "Input Playground ID")
        String gameTeamSide,

        @NotBlank(message = "Input Playground ID")
        String gameType
) {
    
}
