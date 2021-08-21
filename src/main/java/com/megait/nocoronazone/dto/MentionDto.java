package com.megait.nocoronazone.dto;

import com.megait.nocoronazone.domain.Mention;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MentionDto {

    private Long no;
//    private Member member;
//    private String writer;
    private String nickname;
    private String content;
    private String location;
//    private LocalDateTime createdDate;
    private LocalDateTime regdate;

    public Mention toEntity(){
        Mention mention = Mention.builder()
                .no(no)
//                .writer(writer)
                .nickname(nickname)
                .content(content)
                .location(location)
                .regdate(LocalDateTime.now())
                .build();
        return mention;
    }

    @Builder
    public MentionDto(Long no, String nickname, String content, String location, LocalDateTime regdate) {
        this.no = no;
//        this.writer = writer;
        this.nickname = nickname;
        this.content = content;
        this.location = location;
        this.regdate = regdate;
//        this.createdDate = createdDate;
    }
}