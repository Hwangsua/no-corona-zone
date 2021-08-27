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
import java.util.*;

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
        List<Mention> mentionList = new ArrayList<>();


        for (Mention mentions : mentionEntities) {
            Mention mention = Mention.builder()
                    .no(mentions.getNo())
                    .member(mentions.getMember())
                    .content(mentions.getContent())
                    .location(mentions.getLocation())
                    .regdate(mentions.getRegdate())
                    .build();

            mentionList.add(mention);
        }
        return mentionList;
    }

    public Mention getMention(Long no) {
        Optional<Mention> optionalMention = mentionRepository.findById(no);

        if(optionalMention.isEmpty()){
            throw new IllegalArgumentException("wrong mention no");
        }

        return optionalMention.get();
    }





    public List<Mention> getNearLocationMentionList(double currentLatitude,double currentLongitude){

        List<Mention> Mentions = mentionRepository.findAll();
        List<Mention> mentionList = new ArrayList<>();

        for(Mention m : Mentions){

            if ( m.getLatitude()==null || m.getLongitude()==null){
                continue;
            }

            double mentionLatitude = m.getLatitude();
            double mentionLongitude = m.getLongitude();

            if(distanceInKilometerByHaversine(currentLatitude, currentLongitude, mentionLatitude, mentionLongitude)){

                Mention mention = Mention.builder()
                        .no(m.getNo())
                        .member(m.getMember())
                        .content(m.getContent())
                        .location(m.getLocation())
                        .build();

                mentionList.add(mention);
            }

        }

        return mentionList;
    }


    public boolean distanceInKilometerByHaversine(double x1, double y1, double x2, double y2) {
        double distance;
        double radius = 6371; // 지구 반지름(km)
        double toRadian = Math.PI / 180;

        double deltaLatitude = Math.abs(x1 - x2) * toRadian;
        double deltaLongitude = Math.abs(y1 - y2) * toRadian;

        double sinDeltaLat = Math.sin(deltaLatitude / 2);
        double sinDeltaLng = Math.sin(deltaLongitude / 2);
        double squareRoot = Math.sqrt(sinDeltaLat * sinDeltaLat + Math.cos(x1 * toRadian) * Math.cos(x2 * toRadian) * sinDeltaLng * sinDeltaLng);

        distance = 2 * radius * Math.asin(squareRoot);

        if (0 <= distance && distance <= 10) {
            return true;
        }

        return false;
    }


    @Transactional
    public void deleteMention(Long no) {
        mentionRepository.deleteByNo(no);
    }



    @Transactional
    public List<Mention> getMemberMentions(String nickname) {
        List<Mention> mentionEntities = mentionRepository.findByMember_NicknameContainsOrderByRegdateDesc(nickname);
        List<Mention> memberMentionList = new ArrayList<>();

        for (Mention mentions : mentionEntities) {
            Mention mention = Mention.builder()
                    .no(mentions.getNo())
                    .member(mentions.getMember())
                    .content(mentions.getContent())
                    .location(mentions.getLocation())
                    .regdate(mentions.getRegdate())
                    .build();

            memberMentionList.add(mention);

        }

        System.out.println(memberMentionList);
        return memberMentionList;
    }


    @Transactional
    public List<Mention> searchMentions(String keyword) {
        List<Mention> mentionEntities = mentionRepository.findByContentContaining(keyword);
        List<Mention> mentionFormList = new ArrayList<>();

        if (mentionEntities.isEmpty()) return mentionFormList;

        for (Mention mentions : mentionEntities) {
            Mention mention = Mention.builder()
                    .no(mentions.getNo())
                    .member(mentions.getMember())
                    .content(mentions.getContent())
                    .location(mentions.getLocation())
                    .regdate(mentions.getRegdate())
                    .build();

            mentionFormList.add(mention);
        }
            return mentionFormList;
    }


}