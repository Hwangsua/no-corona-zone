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
//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"filePath", "filename"})})
public class ProfileImage {
    @Id@GeneratedValue
    private Long no;

    private String filename;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    //    private String email;
// 이메일은 변함없는 값이니까 email이 좋지않을까?.. 근데 안됨..ㅎ

//    @OneToOne(fetch = FetchType.LAZY)
//    private Member member;
// 멤버랑 이어져야 불러올 수  있지 않을까..ㅜ


    @Transient
    public String getFilePath() {
        if (filename == null || no == null) return null;

        return "/memberImage/" + member.getNo() + "/" + filename;
    }


}
