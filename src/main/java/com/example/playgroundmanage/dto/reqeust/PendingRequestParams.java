package com.example.playgroundmanage.dto.reqeust;

import com.example.playgroundmanage.login.vo.User;
import lombok.Builder;
import lombok.Data;

@Data
public class PendingRequestParams {

    private User host;
    private Long teamId;

    @Builder
    public PendingRequestParams(User host, Long teamId) {
        this.host = host;
        this.teamId = teamId;
    }
}
