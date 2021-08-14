package com.megait.nocoronazone.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class Mention {
    @Id @GeneratedValue
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Member user;

    @Length
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime regdate;

    private Double latitude;

    private Double longitude;

    private String location;

    @OneToMany(mappedBy = "mention", cascade = CascadeType.ALL)
    private List<ReMention> reMentions;

    @Builder
    public Mention(){
        regdate = LocalDateTime.now();
        reMentions = new ArrayList<>();
    }

}
