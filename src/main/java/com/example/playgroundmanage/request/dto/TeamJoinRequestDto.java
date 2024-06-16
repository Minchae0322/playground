package com.example.playgroundmanage.request.dto;

import jakarta.validation.constraints.NotBlank;
import org.aspectj.weaver.ast.Not;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record TeamJoinRequestDto(
        @NotBlank(message = "Input team Id")
        Long teamId,

        @DefaultValue("")
        String introduction
) {
}
