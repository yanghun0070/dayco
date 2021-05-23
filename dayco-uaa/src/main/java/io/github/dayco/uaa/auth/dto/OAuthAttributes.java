package io.github.dayco.uaa.auth.dto;

import java.util.Map;

import io.github.dayco.uaa.social.domain.SocialLogin;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if(SocialLogin.NAVER.name().equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        OAuthAttributes oAuthAttributes = null;
        if(SocialLogin.GOOGLE.name().equals(registrationId)) {
            oAuthAttributes = ofGoogle(userNameAttributeName, attributes);
        } else if(SocialLogin.GITHUB.name().equals(registrationId)) {
            oAuthAttributes = ofGithub(userNameAttributeName, attributes);
        } else if(SocialLogin.KAKAO.name().equals(registrationId)) {
            oAuthAttributes = ofKakao(userNameAttributeName, attributes);
        }
        return oAuthAttributes;
    }

    private static OAuthAttributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                              .name((String) attributes.get("login"))
                              .picture((String) attributes.get("avatar_url"))
                              .attributes(attributes)
                              .nameAttributeKey(userNameAttributeName)
                              .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                              .name((String) attributes.get("name"))
                              .email((String) attributes.get("email"))
                              .picture((String) attributes.get("picture"))
                              .attributes(attributes)
                              .nameAttributeKey(userNameAttributeName)
                              .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                              .name((String) response.get("name"))
                              .email((String) response.get("email"))
                              .picture((String) response.get("profile_image"))
                              .attributes(response)
                              .nameAttributeKey(userNameAttributeName)
                              .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return OAuthAttributes.builder()
                              .name((String) profile.get("nickname"))
                              .picture((String) profile.get("thumbnail_image_url"))
                              .attributes(attributes)
                              .nameAttributeKey(userNameAttributeName)
                              .build();
    }
}
