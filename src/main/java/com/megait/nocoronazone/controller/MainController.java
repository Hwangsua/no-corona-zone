package com.megait.nocoronazone.controller;

import com.megait.nocoronazone.api.VaccineCountVo;
import com.megait.nocoronazone.api.VaccineXml;
import com.google.gson.JsonObject;
import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final VaccineXml vaccineXml;

    private final MemberService memberService;

    // ================= 메인 ============================
    @RequestMapping("/")
    public String index() {

        return "index";
    }

    // ================= 사용자 ============================
    @ResponseBody
    @RequestMapping("/nicknameCk")
    public String checkNickname(@RequestParam String nickname){

        JsonObject object = new JsonObject();

        try{
            memberService.checkNickname(nickname);
            object.addProperty("result", false);
            object.addProperty("message", "사용 불가능한 닉네임입니다.");
        }catch (IllegalArgumentException e){
            object.addProperty("result", true);
            object.addProperty("message","사용 가능한 닉네임입니다.");
        }

        return object.toString();
    }

    @GetMapping ("/email_check_token")
    public String emailCheckToken(String token, String email, Model model){

        try{
            memberService.checkEmailToken(token, email);
        }catch (IllegalArgumentException e){
            model.addAttribute("error","wrong");
            return "member/email_check_result";
        }

        model.addAttribute("success","사용자님");
        return "member/email_check_result";

    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {

        if(errors.hasErrors()){
            System.out.println(errors);
            return "member/signup";
        }

        Member member = memberService.processNewUser(signUpForm);

        //memberService.login(member);

        return "/member/email_check";
    }


    @GetMapping("/login")
    public String login() {
        return "member/login";
    }


    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String coSns_mypage() {
        return "co_sns/profile";
    }

    @GetMapping("/settings")
    public String setUpForm(){
        return "member/settings";
    }

    @PostMapping("/settings")
    public String setUpSubmit(){

        return "member/settings";
    }

    // ================= co_info ============================

    @GetMapping("/vaccine")
    public String vaccine(Model model) {
        VaccineCountVo vaccineCountVo = vaccineXml.getVaccineCount();
        int totalPopulation = vaccineXml.getTotalPopulation();
        model.addAttribute("vaccineCountVo", vaccineCountVo);
        model.addAttribute("totalPopulation", totalPopulation);
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



    // ================= co_sns ============================

    @GetMapping("/timeline_follow")
    public String timelineFollow(){
        return "co_sns/timeline_follow";
    }

    @GetMapping("/timeline_location")
    public String timelineLocation(){
        return "co_sns/timeline_location";
    }

    @GetMapping("/mention/write")
    public String writeTimeline(){
        // ajax
        return "success";
    }

    @GetMapping("/mention_datail")
    public String mentionDetail(){
        return "co_sns/mention_detail";
    }

    @PostMapping("/remention")
    public String remention(){
        // ajax
        return "success";
    }

    @GetMapping("/search")
    public String searchCoSns(){
        return "co_sns/search";
    }

    //TODO - 트렌드, 강사님이 도와주신다고 하심

//    @GetMapping("/following")
//    public String followingList(){
//        return "co_sns/following";
//    }
//
//    @GetMapping("/follower")
//    public String followerList(){
//        return "co_sns/follower";
//    }

//    @GetMapping("/{nickname}")
//    public String profile(@PathVariable String nickname){
//        return "co_sns/profile";
//    }

}
