package com.megait.nocoronazone.service;


import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String nickname,
                           String email) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                // .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {

        return Member.builder()
                .nickname(nickname)
                .email(email)
                .memberType(MemberType.ROLE_GUEST)
                .build();
    }
}