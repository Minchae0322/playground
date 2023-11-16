package com.example.playgroundmanage.type;

import com.example.playgroundmanage.dto.UserProfile;
import lombok.Builder;

import java.util.Map;

public class OAuthAttribute {

    private final String registrationId;
    private final String userNameAttributeName;

    private final Map<String, Object> attributes;

    @Builder
    public OAuthAttribute(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        this.registrationId = registrationId;
        this.userNameAttributeName = userNameAttributeName;
        this.attributes = attributes;
    }

    public UserProfile extract() {
        return UserProfile.builder()
                .username((String) attributes.get(userNameAttributeName))
                .provider(registrationId)
                .build();
    }
}
