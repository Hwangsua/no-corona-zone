package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.Mention;
import com.megait.nocoronazone.form.MentionForm;
import com.megait.nocoronazone.repository.MentionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MentionService {

    private final MentionRepository mentionRepository;


    public void saveMention(Member member, MentionForm mentionForm){

        Mention mention = Mention.builder()
                .member(member)
                .content(mentionForm.getContent())
                .latitude(mentionForm.getLatitude())
                .longitude(mentionForm.getLongitude())
                .location(mentionForm.getLocation())
                .regdate(LocalDateTime.now().withNano(0))
                .build();

        mentionRepository.save(mention);
    }


    @Transactional
    public List<Mention> getMentionlist() {

        List<Mention> mentionEntities = mentionRepository.findAll(Sort.by(Sort.Direction.DESC,"regdate"));
        List<Mention> mentionFormList = new ArrayList<>();

        for (Mention mentions : mentionEntities) {
            Mention mention = Mention.builder()
                    .member(mentions.getMember())
                    .content(mentions.getContent())
                    .location(mentions.getLocation())
                    .regdate(mentions.getRegdate())
                    .build();

            mentionFormList.add(mention);
        }
        return mentionFormList;
    }
//
//    @Transactional
//    public void deletePost(Long id) {
//        mentionRepository.deleteById(id);
//    }
//
//    @Transactional
//    public List<MentionDto> searchPosts(String keyword) {
//        List<Mention> mentionEntities = mentionRepository.findByContentContaining(keyword);
//        List<MentionDto> MentionDtoList = new ArrayList<>();
//
//        if (mentionEntities.isEmpty()) return MentionDtoList;
//
//        for (Mention Mention : mentionEntities) {
//            MentionDtoList.add(this.convertEntityToDto(Mention));
//        }
//
//        return MentionDtoList;
//    }

}