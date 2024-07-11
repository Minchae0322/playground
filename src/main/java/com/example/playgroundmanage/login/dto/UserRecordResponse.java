package com.example.playgroundmanage.login.dto;

import com.example.playgroundmanage.login.vo.UserAthleticsRecord;
import lombok.Builder;
import lombok.Data;


public record UserRecordResponse(

        String userNickname,

        String userProfileImg,

        int ranking,

        int win,

        int draw,

        int lose

) {
    public static UserRecordResponse from(UserAthleticsRecord userAthleticsRecord) {
        return new UserRecordResponse(
                userAthleticsRecord.getUser().getNickname(),
                userAthleticsRecord.getUser().getUserProfileImg().getFileUrl(),
                0,
                userAthleticsRecord.getWin(),
                userAthleticsRecord.getDraw(),
                userAthleticsRecord.getLose()
        );
    }

    public static UserRecordResponse from(UserAthleticsRecord userAthleticsRecord, int ranking) {
        return new UserRecordResponse(
                userAthleticsRecord.getUser().getNickname(),
                userAthleticsRecord.getUser().getUserProfileImg().getFileUrl(),
                ranking,
                userAthleticsRecord.getWin(),
                userAthleticsRecord.getDraw(),
                userAthleticsRecord.getLose()
        );
    }

}
