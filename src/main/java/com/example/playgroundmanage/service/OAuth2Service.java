package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.UserEdit;
import com.example.playgroundmanage.dto.OAuth2UserProfile;
import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.type.OAuthAttributes;
import com.example.playgroundmanage.vo.MyOauth2UserDetails;
import com.example.playgroundmanage.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserProfile OAuth2UserProfile = OAuthAttributes.extract(registrationId, attributes);

        User user = saveOrUpdateUser(OAuth2UserProfile);
        return new MyOauth2UserDetails(user, attributes, Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())));
    }

    private User saveOrUpdateUser(OAuth2UserProfile oAuth2UserProfile) {
       //if(isExistUser(oAuth2UserProfile)) {
            return userRepository.findByUserIdAndProvider(oAuth2UserProfile.getUserId(), oAuth2UserProfile.getProvider())
                    .orElseGet(() -> userRepository.save(oAuth2UserProfile.toEntity()));
       //}
       //return userRepository.save(oAuth2UserProfile.toEntity());
    }

    private boolean isExistUser(OAuth2UserProfile oAuth2UserProfile) {
        return userRepository.existsByUserIdAndProvider(oAuth2UserProfile.getUserId(), oAuth2UserProfile.getProvider());
    }

}
