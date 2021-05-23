package io.github.dayco.uaa.social.domain;

public enum SocialLogin {
    NAVER("naver"),
    GOOGLE("google"),
    GITHUB("github"),
    KAKAO("kakao");

    private String name;

    SocialLogin(String name) {
        this.name = name;
    }

}
