package com.example.playgroundmanage.location.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class CampusResponseDto {

    private Long campusId;

    private String campusName;


    @Builder
    public CampusResponseDto(Long campusId, String campusName) {
        this.campusId = campusId;
        this.campusName = campusName;
    }
}
