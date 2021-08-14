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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"filePath", "filename"})})
public class ProfileImage {
    @Id @GeneratedValue
    private Long no;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileName;

    @OneToOne(fetch = FetchType.LAZY)
    private Member user;

}
