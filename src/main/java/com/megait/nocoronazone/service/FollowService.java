package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Follow;
import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.form.FollowInfoForm;
import com.megait.nocoronazone.repository.FollowRepository;
import com.megait.nocoronazone.repository.MemberRepository;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Validated
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    public Follow getFollow(Member member){
        member = memberRepository.findByNo(member.getNo()).orElseThrow();
        Optional<Follow> memberFollow = followRepository.findByWho(member);

        if(memberFollow.isEmpty()){
            Follow follow = new Follow();
            follow.setWho(member);
            return followRepository.save(follow);
        }
        return memberFollow.get();
    }

    public boolean isFollowStatus(Member who, Member whom){

        Follow findFollow = getFollow(who);
        List<Member> followList = findFollow.getWhom();

        if (followList.contains(whom)){
            return true;
        }
        return false;
    }

    public void follow(Member who, Member whom){
        Follow findFollow = getFollow(who);
        List<Member> followList = findFollow.getWhom();

        if (!followList.contains(whom)){
            findFollow.addFollowMember(whom);
            followRepository.save(findFollow);
        }
    }

    public void unfollow(Member who, Member whom){

        Follow findFollow = getFollow(who);
        List<Member> followList = findFollow.getWhom();

        int index = IntStream.range(0, followList.size())
                .filter(i -> whom.getNo().equals(followList.get(i).getNo()))
                .findFirst().orElse(-1);

        if (index >= 0){
            findFollow.removeFollowMember(index);
            followRepository.save(findFollow);
        }
    }

    public Integer getFollowersNumber(Member member){
        return followRepository.countByWhom(member);
    }

    public Integer getFollowingsNumber(Member member){
        return getFollow(member).getWhom().size();
    }

    public String setUnit(Integer number){

        if(number/1000000 >=1){
            return String.format("%.1f",number/1000000.0) + "M";
        }
        if(number/1000 >=1){
            return String.format("%.1f",number/1000.0) + "k";
        }
        return number.toString();
    }

    public List<Member> getFollowerList(Member whom){
        List<Follow> findFollowList = followRepository.findByWhom(whom);
        List<Member> followerMemberList = new ArrayList<>();
        for(Follow f : findFollowList){
            Member m = f.getWho();
            followerMemberList.add(m);
        }
        return followerMemberList;
    }

    public List<Member> getFollowingList(Member who){
        return  getFollow(who).getWhom();
    }

    public Map<String, Boolean> getFollowerMap(Member loginMember, Member pageMember){
        List<Member> findFollowerList = getFollowerList(pageMember); //페이지 멤버를 팔로워한 사람들을 찾는다.
        Map<String, Boolean> followerMap = new HashMap<String, Boolean>();

        for(Member member : findFollowerList){
            followerMap.put(member.getNickname(),isFollowStatus(loginMember, member));
        }

        return followerMap;
    }

    public Map<String, Boolean> getFollowingMap(Member loginMember, Member pageMember){
        List<Member> findFollowingList = getFollowingList(pageMember);
        Map<String, Boolean> followingMap = new HashMap<String, Boolean>();

        for(Member member : findFollowingList){
            followingMap.put(member.getNickname(),isFollowStatus(loginMember, member));
        }

        return followingMap;
    }


    public FollowInfoForm getFollowInfo(Member loginMember, Member pageMember) {

        FollowInfoForm pageFollowInfo = new FollowInfoForm();
        pageFollowInfo.setFollowingNumber(setUnit(getFollowingsNumber(pageMember)));
        pageFollowInfo.setFollowerNumber(setUnit(getFollowersNumber(pageMember)));


        if (loginMember.getNo() == pageMember.getNo()){
            pageFollowInfo.setFollowingList(getFollowingList(pageMember));
            pageFollowInfo.setFollowerList(getFollowerList(pageMember));
        }else{
            pageFollowInfo.setFollowingMap(getFollowingMap(loginMember,pageMember));
            pageFollowInfo.setFollowerMap(getFollowerMap(loginMember, pageMember));
            pageFollowInfo.setFollowStatus(isFollowStatus(loginMember, pageMember));
        }

        return pageFollowInfo;
    }

}
