package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.Mention;
import com.megait.nocoronazone.form.MentionForm;
import com.megait.nocoronazone.repository.MentionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MentionService {

    private final MentionRepository mentionRepository;
    //final로 설정이 안되어있으면 bean으로 설정이 안된다.

    private static final int BLOCK_PAGE_NUM_COUNT = 10;  // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 10;       // 한 페이지에 존재하는 게시글 수


    public void saveMention(Member member, MentionForm mentionForm){

        Mention mention = Mention.builder()
                .member(member)
                .content(mentionForm.getContent())
                .latitude(mentionForm.getLatitude())
                .longitude(mentionForm.getLongitude())
                .location(mentionForm.getLocation())
                .regdate(LocalDateTime.now())
                .build();

        mentionRepository.save(mention);
    }

//    @Transactional
//    public List<MentionDto> getMentionlist(Integer pageNum) {
//        Page<Mention> page = mentionRepository.findAll(PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createdDate")));
//
//        List<Mention> mentionEntities = page.getContent();
//        List<MentionDto> mentionDtoList = new ArrayList<>();
//
//        for (Mention mention : mentionEntities) {
//            mentionDtoList.add(this.convertEntityToDto(mention));
//        }
//
//        return mentionDtoList;
//    }
//
//    @Transactional
//    public List<MentionDto> getMentionlist() {
//        List<Mention> mentionEntities = mentionRepository.findAll();
//        List<MentionDto> mentionDtoList = new ArrayList<>();
//
//        for ( Mention mention : mentionEntities) {
//            MentionDto mentionDto = MentionDto.builder()
//                    .no(mention.getNo())
////                  .member(Mention.getMember())
//                    .nickname(mention.getNickname())
//                    .content(mention.getContent())
//                    .location(mention.getLocation())
////                    .createdDate(mention.getCreatedDate())
//                    .build();
//
//            mentionDtoList.add(mentionDto);
//        }
//
//        return mentionDtoList;
//    }
//
//
//    @Transactional
//    public Long getBoardCount() {
//        return mentionRepository.count();
//    }
//
//    @Transactional
//    public MentionDto getPost(Long id) {
//        Optional<Mention> MentionWrapper = mentionRepository.findById(id);
//        Mention mention = MentionWrapper.get();
//
//        return this.convertEntityToDto(mention);
//    }
//
//    @Transactional
//    public Long savePost(MentionDto MentionDto) {
//        return mentionRepository.save(MentionDto.toEntity()).getNo();
//    }
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
//
//    public Integer[] getPageList(Integer curPageNum) {
//        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
//
//        // 총 게시글 갯수
//        Double postsTotalCount = Double.valueOf(this.getBoardCount());
//
//        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산
//        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));
//
//        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
//        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
//                ? curPageNum + BLOCK_PAGE_NUM_COUNT
//                : totalLastPageNum;
//
//        // 페이지 시작 번호 조정
//        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;
//
//        // 페이지 번호 할당
//        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
//            pageList[idx] = val;
//        }
//
//        return pageList;
//    }
//
//    private MentionDto convertEntityToDto(Mention mention) {
//        return MentionDto.builder()
//                .no(mention.getNo())
////                .member(Mention.getMember())
//                .nickname(mention.getNickname())
//                .content(mention.getContent())
//                .location(mention.getLocation())
////                .createdDate(mention.getCreatedDate())
//                .regdate(mention.getRegdate())
//                .build();
//    }
}
