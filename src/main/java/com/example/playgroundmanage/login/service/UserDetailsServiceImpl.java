package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.login.dto.OAuth2UserProfile;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.type.OAuthAttributes;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.login.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(MyUserDetails::new).orElseThrow();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = getOAuth2User(userRequest);
        OAuth2UserProfile oAuth2UserProfile = getOAuth2UserProfile(userRequest.getClientRegistration().getRegistrationId(), oAuth2User);
        User user = getUserInDatabase(oAuth2UserProfile);

        return new MyUserDetails(user, oAuth2UserProfile.getAttributes());
    }

    private OAuth2User getOAuth2User(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        return delegate.loadUser(userRequest);
    }

    public OAuth2UserProfile getOAuth2UserProfile(String registrationId, OAuth2User oAuth2User) {
        return OAuthAttributes.extract(registrationId, oAuth2User.getAttributes());
    }

    private User getUserInDatabase(OAuth2UserProfile oAuth2UserProfile) {
        return userRepository.findByUsernameAndProvider(oAuth2UserProfile.getUsername(), oAuth2UserProfile.getProvider())
                .orElseGet(() -> userRepository.save(oAuth2UserProfile.toEntity()));
    }

}
