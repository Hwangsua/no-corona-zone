package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String nickname;
    private String email;

    public SessionUser(Member user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}