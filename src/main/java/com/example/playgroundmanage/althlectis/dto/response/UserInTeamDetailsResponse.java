package com.example.playgroundmanage.althlectis.dto.response;

import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.type.GameTeamSide;

public record UserInTeamDetailsResponse(
        Long userId,
        String userNickname,
        String userRole,

        String userProfileImg
) {

    public static UserInTeamDetailsResponse of(AthleticsParticipant gameParticipant) {
        return new UserInTeamDetailsResponse(
                gameParticipant.getId(),
                gameParticipant.getUser().getNickname(),
                gameParticipant.getUser().getRole().getValue(),
                gameParticipant.getUser().getUserProfileImg().getFileUrl()
        );
    }

}
