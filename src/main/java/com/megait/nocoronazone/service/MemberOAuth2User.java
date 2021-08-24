package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;

@Getter
public class MemberOAuth2User extends DefaultOAuth2User {
    private final Member member;

    public MemberOAuth2User(Member member, OAuthAttributes attributes){
        super(Collections.singleton(new SimpleGrantedAuthority(member.getMemberType().name())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
        this.member = member;
    }
}