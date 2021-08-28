package com.megait.nocoronazone.service
        ;
import com.megait.nocoronazone.domain.AuthType;
import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Slf4j
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;

    private final AuthType authType;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture, AuthType authType) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.authType = authType;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        if ("facebook".equals(registrationId)) {
            return ofFacebook("id",attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .authType(AuthType.GOOGLE)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .authType(AuthType.NAVER)
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofFacebook(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .authType(AuthType.FACEBOOK)
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


//    private static OAuthAttributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes) {
//        String email = (String) attributes.get("email");
//        if (email == null) {
//            email = ((String) attributes.get("name")) + "@facebook.com";
//        }
//
//        Map<String, Object> picture = (Map<String, Object>) attributes.get("picture");
//        Map<String, Object> picture_data = (Map<String, Object>) picture.get("data");
//        String picture_url = (String) picture_data.get("url");
//
//        return OAuthAttributes.builder()
//                .name((String) attributes.get("name"))
//                .email(email)
//                .picture(picture_url)
//                .authType(AuthType.FACEBOOK)
//                .attributes(attributes)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }

    public Member toEntity() {
        int num = (int)(Math.random()*100000);
        String nickname = Integer.toString(num);

        return Member.builder()
                .name(name)
                .email(email)
                .memberType(MemberType.ROLE_USER)
                .certification(true)
                .introduce("{noop}")
                .nickname(nickname)
                .authType(authType)
                .emailVerified(true)
                .password("{noop}")
                .build();
    }
}
