package com.example.playgroundmanage.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlaygroundDto {

    private Long playgroundId;


    @Builder
    public PlaygroundDto(Long playgroundId) {
        this.playgroundId = playgroundId;
    }
}
