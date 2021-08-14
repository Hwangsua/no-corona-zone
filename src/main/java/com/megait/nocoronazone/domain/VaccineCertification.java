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
public class VaccineCertification {
    @Id @GeneratedValue
    private Long no;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    String fileName;

    @OneToOne(fetch = FetchType.LAZY)
    private Member user;


}
