package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.OAuth2UserProfile;
import com.example.playgroundmanage.dto.UserSignupForm;
import com.example.playgroundmanage.exception.ExistUserException;
import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.type.OAuthAttributes;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.MyUserDetails;
import com.example.playgroundmanage.vo.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.playgroundmanage.validator.UserValidator.validateUser;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(MyUserDetails::new).orElseThrow();
    }

    @Transactional
    public void signup(UserSignupForm userSignupForm) {
        validateUser(userSignupForm.getUsername(), userSignupForm.getPassword());
        if(userRepository.existsByUsername(userSignupForm.getUsername())) {
            throw new ExistUserException();
        }
        User user = User.builder()
                .username(userSignupForm.getUsername())
                .password(passwordEncoder.encode(userSignupForm.getPassword()))
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = getOAuth2User(userRequest);
        OAuth2UserProfile oAuth2UserProfile = getOAuth2UserProfile(userRequest.getClientRegistration().getRegistrationId(), oAuth2User);
        User user = getUserInDatabase(oAuth2UserProfile);

        return new MyOauth2UserDetails(user, oAuth2UserProfile.getAttributes(), Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())));
    }

    private OAuth2User getOAuth2User(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        return delegate.loadUser(userRequest);
    }

    protected OAuth2UserProfile getOAuth2UserProfile(String registrationId, OAuth2User oAuth2User) {
        return OAuthAttributes.extract(registrationId, oAuth2User.getAttributes());
    }

    private User getUserInDatabase(OAuth2UserProfile oAuth2UserProfile) {
        return userRepository.findByUsernameAndProvider(oAuth2UserProfile.getUsername(), oAuth2UserProfile.getProvider())
                .orElseGet(() -> userRepository.save(oAuth2UserProfile.toEntity()));
    }

}
