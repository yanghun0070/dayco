package com.ykgroup.dayco.uaa.auth.config;


import javax.servlet.http.HttpSession;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ykgroup.dayco.uaa.auth.dto.OAuthAttributes;
import com.ykgroup.dayco.uaa.auth.dto.SessionUser;
import com.ykgroup.dayco.uaa.manager.domain.UserAuthorization;
import com.ykgroup.dayco.uaa.user.domain.User;
import com.ykgroup.dayco.uaa.user.infra.UserJpaRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private UserJpaRepository userJpaRepository;
    private HttpSession httpSession;

    public CustomOAuth2UserService(UserJpaRepository userJpaRepository,
                                   HttpSession httpSession) {
        this.userJpaRepository = userJpaRepository;
        this.httpSession = httpSession;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                                                        oAuth2User.getAttributes());

        User user = saveOrUpdate(registrationId, attributes);
        httpSession.setAttribute("user", new SessionUser(user, registrationId));

        return new DefaultOAuth2User(
                user.getAuthorities(),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(String registrationId, OAuthAttributes attributes) {
        User user;
        // Github 일 경우, Email 이 없다.
        if("github".equals(registrationId)) {
            User changedUser = new User(attributes.getName(), "social", attributes.getPicture());
            changedUser.addUserAuthorization(new UserAuthorization(changedUser, "USER"));
            user = userJpaRepository.findByUserId(attributes.getName())
                                    .map(entity -> entity.update(attributes.getName(),
                                                                 attributes.getPicture()))
                                    .orElse(changedUser);
        } else {
            User changedUser = new User(attributes.getName(),
                                        "social",
                                        attributes.getEmail(),
                                        attributes.getPicture());
            changedUser.addUserAuthorization(new UserAuthorization(changedUser, "USER"));
            user = userJpaRepository.findByEmailEmail(attributes.getEmail())
                                    .map(entity -> entity.update(attributes.getName(),
                                                                 attributes.getPicture()))
                                    .orElse(changedUser);
        }
        return userJpaRepository.save(user);
    }
}
