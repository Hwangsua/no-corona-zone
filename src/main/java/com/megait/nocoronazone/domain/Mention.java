package com.megait.nocoronazone.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

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
@EqualsAndHashCode(callSuper = false, exclude = {"reMention"})
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mention")
public class Mention {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long no;

    @ManyToOne(cascade = CascadeType.ALL)
    private Member member;
//    private String writer;

    //    @Transient
    private String nickname;

    //    @PostLoad
    private void setNickname(){
        this.nickname = member.getNickname();
    }

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
