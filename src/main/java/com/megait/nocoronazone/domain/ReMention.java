package com.megait.nocoronazone.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class ReMention {
    @Id @GeneratedValue
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mention mention;

    @Column(nullable = false)
    private String content;

    private LocalDateTime regdate;

    @Builder
    public ReMention(){
        regdate = LocalDateTime.now();
    }

}
