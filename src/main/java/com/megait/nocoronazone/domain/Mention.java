package com.megait.nocoronazone.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Data
public class Mention {

    @Id @GeneratedValue
    private Long no;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    @Length
    @NotNull
    private String content;

    @NotNull
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