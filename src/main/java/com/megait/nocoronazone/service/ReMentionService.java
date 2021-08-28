package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.Mention;
import com.megait.nocoronazone.domain.ReMention;
import com.megait.nocoronazone.form.ReMentionForm;
import com.megait.nocoronazone.repository.MentionRepository;
import com.megait.nocoronazone.repository.ReMentionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReMentionService {

    private final ReMentionRepository reMentionRepository;
    private final MentionRepository mentionRepository;

    public void saveReMention(Member member, Mention parentMention ,ReMentionForm reMentionForm){

        ReMention reMention = ReMention.builder()
                .mention(parentMention)
                .member(member)
                .content(reMentionForm.getContent())
                .regdate(LocalDateTime.now())
                .build();

        reMentionRepository.save(reMention);
    }

    public List<ReMention> getReMentionList(Mention parentMention) {

        List<ReMention> reMentions = reMentionRepository.findByMentionOrderByRegdateDesc(parentMention);
        List<ReMention> reMentionList = new ArrayList<>();

        for (ReMention r : reMentions){
            ReMention reMention = ReMention.builder()
                    .no(r.getNo())
                    .member(r.getMember())
                    .content(r.getContent())
                    .nlString(System.getProperty("line.separator").toString())
                    .regdate(r.getRegdate())
                    .build();

            reMentionList.add(reMention);
        }
        System.out.println(reMentionList);
        System.out.println(reMentionList.toString());
        return reMentionList;
    }


}
