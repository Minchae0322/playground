package com.example.playgroundmanage.team.dto;

import lombok.Builder;
import lombok.Data;

public class TeamDto {

    @Data
    public static class TeamResponseDto {
        private Long teamId;
        private String teamName;

        private String teamProfileImg;

        private String sportsEvent;

        private String description;

        private Long leaderId;

        private String leaderName;

        @Builder
        public TeamResponseDto(Long teamId, String teamName, String teamProfileImg, String sportsEvent, Long leaderId, String leaderName, String description) {
            this.teamId = teamId;
            this.teamName = teamName;
            this.teamProfileImg = teamProfileImg;
            this.sportsEvent = sportsEvent;
            this.leaderId = leaderId;
            this.leaderName = leaderName;
            this.description = description;
        }

    }
}
