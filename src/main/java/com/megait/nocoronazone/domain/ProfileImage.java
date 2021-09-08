package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class ProfileImage {
    @Id@GeneratedValue

    private Long no;

    private String filename;

    @OneToOne
    private Member member;

    @Override
    public String toString() {
        return "ProfileImage{" +
                "no=" + no +
                ", filename='" + filename;
    }
}
