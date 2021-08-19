//package com.megait.nocoronazone.service;
//
//
//import com.megait.nocoronazone.domain.Member;
//import com.megait.nocoronazone.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpSession;
//import java.util.Collections;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class SnsMemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//    private final MemberRepository memberRepository;
//    private final HttpSession httpSession;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String userNameAttributeName = userRequest.getClientRegistration()
//                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//
//        OAuthAttributes attributes = OAuthAttributes.
//                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        Member member = saveOrUpdate(attributes);
//        httpSession.setAttribute("user", new SessionUser(member));
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority(member.getMemberType().name())),
//                attributes.getAttributes(),
//                attributes.getNameAttributeKey());
//
//    }
//
//    private Member saveOrUpdate(OAuthAttributes attributes) {
//        Member member = memberRepository.findByEmail(attributes.getEmail())
//                .map(entity -> {
//
//                    entity.setNickname(attributes.getNickname());
//                    entity.setEmail(attributes.getEmail());
//                    return entity;
//                })
//                .orElse(attributes.toEntity());
//
//        return memberRepository.save(member);
//
//    }
//}
