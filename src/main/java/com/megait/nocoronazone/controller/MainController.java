package com.megait.nocoronazone.controller;

import com.megait.nocoronazone.api.VaccineCountVo;
import com.megait.nocoronazone.api.VaccineXml;
import com.google.gson.JsonObject;
import com.megait.nocoronazone.domain.ChatMessage;
import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.Mention;
import com.megait.nocoronazone.form.MentionForm;
import com.megait.nocoronazone.form.ReMentionForm;
import com.megait.nocoronazone.form.LocationSearchForm;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.service.CustomOAuth2UserService;
import com.megait.nocoronazone.service.MemberService;
import com.megait.nocoronazone.service.DetailSafetyService;
import com.megait.nocoronazone.service.MentionService;
import com.megait.nocoronazone.service.ReMentionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final DetailSafetyService detailSafetyService;
    private final VaccineXml vaccineXml;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpSession httpSession;
    private final MemberService memberService;
    private final MentionService mentionService;
    private final ReMentionService reMentionService;

    // ================= 메인 ============================
    @RequestMapping("/")
    public String index(Model model) {


//        System.out.println(detailSafetyService.getDetailSafetytoAlpha("Chuncheon-si"));
        return "index";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        log.info("User : {}", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
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

    @PostMapping("/login")
    public String login(Member member) {
        memberService.login(member);
        return "index";
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

    //타임라인(팔로우)
    @GetMapping("/timeline_follow")
    public String timelineFollow(Model model){
        List<Mention> mentionFormList = mentionService.getMentionlist();

        model.addAttribute("member", memberService);
        model.addAttribute("mentionFormList", mentionFormList);
        model.addAttribute("mentionForm", new MentionForm());
        return "co_sns/timeline_follow";
    }

    //타임라인(위치)
    @GetMapping("/timeline_location")
    public String timelineLocation(Model model)
    {
        System.out.println("Get");
        model.addAttribute("locationSearchForm", new LocationSearchForm());
        return "co_sns/timeline_location";

    }

    @PostMapping("/timeline_location")
    public String searchLocation(@Valid LocationSearchForm locationSearchForm, Errors errors, Model model)
    {
        System.out.println("Post");
        double lx = locationSearchForm.getLatitude();
        double ly = locationSearchForm.getLongitude();

        model.addAttribute("mentionList", mentionService.getNearLocationMentionList(lx, ly));

        return "co_sns/timeline_location";

    }

    @PostMapping("/timeline_follow")
    public String write(@AuthenticationMember Member member,MentionForm mentionForm){

        if (member == null){
            return "redirect:login";
        }

        mentionService.saveMention(member, mentionForm);

        return "redirect:timeline_follow";
    }

    @GetMapping("/mention_detail/{no}")
    public String mentionDetail(@PathVariable Long no, Model model){

        try {
            Mention parentMention = mentionService.getMention(no);
            model.addAttribute("mention", parentMention );
            model.addAttribute("reMentionForm", new ReMentionForm());
            model.addAttribute("reMentionList",reMentionService.getReMentionList(parentMention));
        }catch (IllegalArgumentException e){
            return "co_sns/timeline_location";
        }
        return "co_sns/mention_detail";
    }


    @PostMapping("/remention")
    public String remention(@AuthenticationMember Member member, ReMentionForm reMentionForm, Model model){

        try {
            Mention parentMention = mentionService.getMention(reMentionForm.getParentMentionNo());
            reMentionService.saveReMention(member, parentMention,reMentionForm);
            model.addAttribute("reMentionList",reMentionService.getReMentionList(parentMention));
        }catch (IllegalArgumentException e){
            return "co_sns/timeline_location";
        }

        return "co_sns/mention_detail :: #re-mention-list";
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
