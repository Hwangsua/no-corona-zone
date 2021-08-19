package com.megait.nocoronazone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @RequestMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "member/signup";
    }

    @GetMapping("/profile")
    public String coSns_mypage() {
        return "co_sns/profile";
    }

    @GetMapping("/cosns")
    public String timelineFollow(){
        return "co_sns/timeline_follow";
    }

    @GetMapping("/timeline_location")
    public String timelineLocation(){
        return "co_sns/timeline_location";
    }

    @GetMapping("/settings")
    public String settings() { return "member/settings"; }

    @GetMapping("/vaccine")
    public String vaccine() {
        return "co_info/vaccine";
    }

    @GetMapping("/clinic")
    public String clinic() {
        return "co_info/clinic";
    }

    @GetMapping("/news")
    public String co_info_news() { return "/co_info/main";}

    @GetMapping("/video")
    public String co_info_video() { return "/co_info/video";}

    @GetMapping("/article")
    public String article() {
        return "co_info/article";
    }
}
