package com.megait.nocoronazone.form;

import com.megait.nocoronazone.domain.Member;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FollowInfoForm {

    private String followerNumber;

    private String followingNumber;

    private List<Member> followerList;

    private List<Member> followingList;

    private Map<String, Boolean> followerMap;

    private Map<String, Boolean> followingMap;

    private boolean followStatus;


}
