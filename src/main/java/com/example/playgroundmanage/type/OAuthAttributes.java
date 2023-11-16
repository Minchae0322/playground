package com.example.playgroundmanage.type;

import com.example.playgroundmanage.dto.UserProfile;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    WECHAT("wechat", attribute -> UserProfile.builder()
            .username((String) attribute.get("sub"))
            .provider("wechat")
            .build()),

    NAVER("naver",  attribute -> UserProfile.builder()
            .username((String) attribute.get("id"))
            .provider("naver")
            .build());

    private final String registrationId; // Service used for login (e.g., google, naver...)

    private final Function<Map<String, Object>, UserProfile> userProfileExtractor; // Function to extract UserProfile from user attributes

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> userProfileExtractor) {
        this.registrationId = registrationId;
        this.userProfileExtractor = userProfileExtractor;
    }

    /**
     * Extracts UserProfile based on the registrationId and user attributes.
     *
     * @param registrationId Service used for login (e.g., google, naver...)
     * @param attributes     User attributes provided by the OAuth provider
     * @return UserProfile extracted from the attributes
     * @throws IllegalArgumentException if no matching registrationId is found
     */
    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported registrationId: " + registrationId))
                .userProfileExtractor.apply(attributes);
    }
}
