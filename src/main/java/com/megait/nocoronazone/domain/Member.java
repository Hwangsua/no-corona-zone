package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Member {
    @Id @GeneratedValue
    private Long no;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String introduce;

    @Column(nullable = false)
    private boolean certification;

    private String emailCheckToken;

    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    private YellowCard yellowCard;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    public void generateEmailCheckToken(){
        emailCheckToken = UUID.randomUUID().toString();
    }

    public boolean isValidToken(String token) {
        return emailCheckToken.equals(token);
    }

    public void completeSignup(){
        emailVerified = true;
    }
}
