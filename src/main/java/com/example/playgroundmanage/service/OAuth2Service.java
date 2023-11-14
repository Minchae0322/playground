package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.UserEdit;
import com.example.playgroundmanage.dto.UserProfile;
import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.type.OAuthAttributes;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);
        Map<String, Object> customAttribute = getCustomAttribute(userRequest, attributes, userProfile);

        saveOrUpdateUser(userProfile);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(UserRole.USER.getValue())),
                customAttribute, userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }

    private Map<String, Object> getCustomAttribute(OAuth2UserRequest userRequest,
                                                   Map<String, Object> attributes,
                                                   UserProfile userProfile) {
        Map<String, Object> customAttribute = new ConcurrentHashMap<>();

        customAttribute.put(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(), attributes.get(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()));
        customAttribute.put("provider", userRequest.getClientRegistration().getRegistrationId());
        customAttribute.put("name", userProfile.getUsername());

        return customAttribute;
    }

    private User saveOrUpdateUser(UserProfile userProfile) {
        String username = userProfile.getUsername();
        String provider = userProfile.getProvider();

        return userRepository.findByUsernameAndProvider(username, provider)
                .map(entity -> entity.update(UserEdit.builder().username(username).build()))
                .orElseGet(() -> userRepository.save(userProfile.toEntity()));
    }
}
