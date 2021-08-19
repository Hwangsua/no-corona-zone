package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false)
    private String nickname;

    private String introduce;

    @Column(nullable = false)
    private boolean certification;

    private String emailCheckToken;

    @Enumerated
    private YellowCard yellowCard;

    @Enumerated
    private MemberType memberType;

}
