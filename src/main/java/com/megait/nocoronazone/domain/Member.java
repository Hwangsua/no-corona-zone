package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.*;

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
