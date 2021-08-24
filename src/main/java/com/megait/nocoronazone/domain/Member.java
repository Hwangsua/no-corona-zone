package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

// @DynamicInsert : 현재 들어있는(설정되어있는) 값만 넣는다. @columnDefault로 값 설정
// @DynamicUpdate : 현재 엔티티에 적용되어있는 설정된 값만 변경한다.

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

    private String name;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Column(nullable = false)
    private String nickname;

    private String introduce;

    @Column(nullable = false)
    private boolean certification;

    private String emailCheckToken;

    @Column(nullable = false)
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
