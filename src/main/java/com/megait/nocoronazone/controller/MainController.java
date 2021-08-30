package com.megait.nocoronazone.controller;

import com.google.gson.JsonObject;
import com.megait.nocoronazone.api.VaccineCountVo;
import com.megait.nocoronazone.api.VaccineXml;
import com.megait.nocoronazone.domain.*;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.form.MentionForm;
import com.megait.nocoronazone.form.ReMentionForm;
import com.megait.nocoronazone.form.LocationSearchForm;
import com.megait.nocoronazone.domain.Mention;
import com.megait.nocoronazone.form.*;
import com.megait.nocoronazone.service.*;
import com.megait.nocoronazone.service.DetailSafetyService;
import com.megait.nocoronazone.service.CustomOAuth2UserService;
import com.megait.nocoronazone.service.MemberService;
import com.megait.nocoronazone.service.MentionService;
import com.megait.nocoronazone.service.SafetyService;
import com.megait.nocoronazone.service.ReMentionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final DetailSafetyService detailSafetyService;
    private final SafetyService safetyService;
    private final DistancingService distancingService;
    private final VaccineXml vaccineXml;
    private final ArticleService articleService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final HttpSession httpSession;
    private final MemberService memberService;
    private final MentionService mentionService;
    private final ReMentionService reMentionService;

    String colorConfirmed = "235, 64, 52"; // red
    String colorDensity = "168, 118, 0"; // yellow
    String colorDistancing = "124, 0, 173"; // purple

    // 전체
    String[] City = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};
    String[] City2 = {"Seoul", "Busan", "Daegu", "Incheon", "Gwangju", "Daejeon", "Ulsan", "Gyeonggi", "Gangwon", "Chungbuk", "Chungnam", "Jeonbuk", "Jeonnam", "Gyeongbuk", "Gyeongnam", "Jeju", "Sejong"};
    // 충청북도
    String[] cbDistrict = {"Boeun-gun", "Cheongju-si", "Chungju-si", "Danyang-gun", "Goesan-gun", "Jecheon-si", "Jeungpyeong-gun", "Jincheon-gun", "Okcheon-gun", "Eumseong-gun", "Yeongdong-gun"};
    // 충청남도
    String[] cnDistrict = {"Dangjin-si", "Seosan-si", "Nonsan-si", "Cheonan-si", "Gongju-si", "Boryeong-si", "Asan-si", "Gyeryong-si", "Geumsan-gun", "Buyeo-gun", "Seocheon-gun", "Cheongyang-gun", "Hongseong-gun", "Yesan-gun", "Taean-gun"};
    // 경상북도
    String[] gbDistrict = {"Pohang-si", "Gyeongju-si", "Gimcheon-si", "Andong-si", "Gumi-si", "Yeongju-si", "Yeongcheon-si", "Sangju-si", "Mungyeong-si", "Gyeongsan-si", "Gunwi-gun", "Uiseong-gun", "Cheongsong-gun", "Yeongyang-gun", "Yeongdeok-gun", "Cheongdo-gun", "Goryeong-gun", "Seongju-gun", "Chilgok-gun", "Yecheon-gun", "Bonghwa-gun", "Uljin-gun", "Ulleung-gun"};
    // 경상남도
    String[] gnDistrict = {"Changwon-si", "Jinju-si", "Tongyeong-si", "Sacheon-si", "Gimhae-si", "Miryang-si", "Geoje-si", "Yangsan-si", "Uiryeong-gun", "Haman-gun", "Changnyeong-gun", "Goseong-gun", "Namhae-gun", "Hadong-gun", "Sancheong-gun", "Hamyang-gun", "Geochang-gun", "Hapcheon-gun"};
    // 전라북도
    String[] jbDistrict = {"Jeonju-si", "Gunsan-si", "Iksan-si", "Jeongeup-si", "Namwon-si", "Gimje-si", "Wanju-gun", "Jinan-gun", "Muju-gun", "Jangsu-gun", "Imsil-gun", "Sunchang-gun", "Gochang-gun", "Buan-gun"};
    // 전라남도
    String[] jnDistrict = {"Mokpo-si", "Yeosu-si", "Suncheon-si", "Naju-si", "Gwangyang-si", "Damyang-gun", "Gokseong-gun", "Gurye-gun", "Goheung-gun", "Boseong-gun", "Hwasun-gun", "Jangheung-gun", "Gangjin-gun", "Haenam-gun", "Yeongam-gun", "Muan-gun", "Hampyeong-gun", "Yeonggwang-gun", "Jangseong-gun", "Wando-gun", "Jindo-gun", "Sinan-gun"};
    // 경기
    String[] ggDistrict = {"Suwon-si", "Seongnam-si", "Uijeongbu-si", "Anyang-si", "Bucheon-si", "Gwangmyeong-si", "Pyeongtaek-si", "Dongducheon-si", "Ansan-si", "Goyang-si", "Gwacheon-si", "Guri-si", "Namyangju-si", "Osan-si", "Siheung-si", "Gunpo-si", "Uiwang-si", "Hanam-si", "Yongin-si", "Paju-si", "Icheon-si", "Anseong-si", "Gimpo-si", "Hwaseong-si", "Gwangju-si", "Yangju-si", "Pocheon-si", "Yeoju-si", "Yeoncheon-gun", "Gapyeong-gun", "Yangpyeong-gun"};
    // 서울
    String[] seoulDistrict = {"Jongno-gu", "Jung-gu", "Yongsan-gu", "Seongdong-gu", "Gwangjin-gu", "Dongdaemun-gu", "Jungnang-gu", "Seongbuk-gu", "Gangbuk-gu", "Dobong-gu", "Nowon-gu", "Eunpyeong-gu", "Seodaemun-gu", "Mapo-gu", "Yangcheon-gu", "Gangseo-gu", "Guro-gu", "Geumcheon-gu", "Yeongdeungpo-gu", "Dongjak-gu", "Gwanak-gu", "Seocho-gu", "Gangnam-gu", "Songpa-gu", "Gangdong-gu"};
    // 부산
    String[] busanDistrict = {"Jung-gu", "Seo-gu", "Dong-gu", "Yeongdo-gu", "Busanjin-gu", "Dongnae-gu", "Nam-gu", "Buk-gu", "Haeundae-gu", "Saha-gu", "Geumjeong-gu", "Gangseo-gu", "Yeonje-gu", "Suyeong-gu", "Sasang-gu", "Gijang-gun"};
    // 강원
    String[] gangwonDistrict = {"Chuncheon-si", "Wonju-si", "Gangneung-si", "Donghae-si", "Taebaek-si", "Sokcho-si", "Samcheok-si", "Hongcheon-gun", "Hoengseong-gun", "Yeongwol-gun", "Pyeongchang-gun", "Jeongseon-gun", "Cheorwon-gun", "Hwacheon-gun", "Yanggu-gun", "Inje-gun", "Goseong-gun", "Yangyang-gun"};
    // 제주
    String[] jejuDistrict = {"Jeju-si", "Seogwipo-si"};
    // 울산
    String[] ulsanDistrict = {"Jung-gu", "Nam-gu", "Dong-gu", "Buk-gu", "Ulju-gun"};
    // 인천
    String[] incheonDistrict = {"Jung-gu", "Dong-gu", "Michuhol-gu", "Yeonsu-gu", "Namdong-gu", "Bupyeong-gu", "Gyeyang-gu", "Seo-gu", "Ganghwa-gun", "Ongjin-gun"};
    // 광주
    String[] gwangjuDistrict = {"Dong-gu", "Seo-gu", "Nam-gu", "Buk-gu", "Gwangsan-gu"};
    // 대구
    String[] daeguDistrict = {"Jung-gu", "Dong-gu", "Seo-gu", "Nam-gu", "Buk-gu", "Suseong-gu", "Dalseo-gu", "Dalseong-gun"};

    // ================= 메인 ============================
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("member", memberService);


        model.addAttribute("confirmedSUM", safetyService.getConfirmedSUM());
        model.addAttribute("safetyList", safetyService.getSafetyList());
        model.addAttribute("color", colorConfirmed);
        for(int i = 0; i < City.length; ++i){
            model.addAttribute(City2[i], safetyService.getConfirmedtoAlpha(City[i]));
        }
        return "index";
    }

    @GetMapping("/infection")
    public String infection(Model model) {
        model.addAttribute("confirmedSUM", safetyService.getConfirmedSUM());
        model.addAttribute("safetyList", safetyService.getSafetyList());
        model.addAttribute("color", colorConfirmed);
        for(int i = 0; i < City.length; ++i){
            model.addAttribute(City2[i], safetyService.getConfirmedtoAlpha(City[i]));
        }
        return "index";
    }

    @GetMapping("/density")
    public String density(Model model) {
        model.addAttribute("confirmedSUM", safetyService.getConfirmedSUM());
        model.addAttribute("safetyList", safetyService.getSafetyList());
        model.addAttribute("color", colorDensity);
        for(int i = 0; i < City.length; ++i){
            model.addAttribute(City2[i], safetyService.getSafetytoAlpha(City[i]));
        }
        return "index";
    }

    @GetMapping("/distancing")
    public String distancing(Model model) {
        model.addAttribute("confirmedSUM", safetyService.getConfirmedSUM());
        model.addAttribute("safetyList", safetyService.getSafetyList());
        model.addAttribute("color", colorDistancing);
        for(int i = 0; i < City.length; ++i){
            model.addAttribute(City2[i], distancingService.getDistancingtoAlpha(City[i]));
            model.addAttribute(City2[i] + "d", distancingService.getDistancing(City[i]));
        }
        return "co_info/distancing";
    }

    @GetMapping("/detail")
    public String detail(Model model, @Param(value = "district")String district) {
        model.addAttribute("confirmedSUM", safetyService.getConfirmedSUM());
        model.addAttribute("safetyList", safetyService.getSafetyList());
        model.addAttribute("color", colorDensity);
        if (district.equals("Seoul")){
            for(int i = 0; i < seoulDistrict.length; ++i){
                model.addAttribute(("Seoul-" + seoulDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Seoul-" + seoulDistrict[i]));
            }
            return "map/seoul";
        } else if (district.equals("Chungbuk")) {
            for (int i = 0; i < cbDistrict.length; ++i) {
                model.addAttribute(("Chungcheongbuk-do-" + cbDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Chungcheongbuk-do-" + cbDistrict[i]));
            }
            return "map/chungbuk";
        } else if (district.equals("Chungnam")) {
            for (int i = 0; i < cnDistrict.length; ++i) {
                model.addAttribute(("Chungcheongnam-do-" + cnDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Chungcheongnam-do-" + cnDistrict[i]));
            }
            return "map/chungnam";
        } else if (district.equals("Gyeongbuk")) {
            for (int i = 0; i < gbDistrict.length; ++i) {
                model.addAttribute(("Gyeongsangbuk-do-" + gbDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Gyeongsangbuk-do-" + gbDistrict[i]));
            }
            return "map/gyeongbuk";
        } else if (district.equals("Gyeongnam")) {
            for (int i = 0; i < gnDistrict.length; ++i) {
                model.addAttribute(("Gyeongsangnam-do-" + gnDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Gyeongsangnam-do-" + gnDistrict[i]));
            }
            return "map/gyeongnam";
        } else if (district.equals("Jeonbuk")) {
            for (int i = 0; i < jbDistrict.length; ++i) {
                model.addAttribute(("Jeollabuk-do-" + jbDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Jeollabuk-do-" + jbDistrict[i]));
            }
            return "map/jeonbuk";
        } else if (district.equals("Jeonnam")) {
            for (int i = 0; i < jnDistrict.length; ++i) {
                model.addAttribute(("Jeollanam-do-" + jnDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Jeollanam-do-" + jnDistrict[i]));
            }
            return "map/jeonnam";
        } else if (district.equals("Gyeonggi")) {
            for (int i = 0; i < ggDistrict.length; ++i) {
                model.addAttribute(("Gyeonggi-do-" + ggDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Gyeonggi-do-" + ggDistrict[i]));
            }
            return "map/gyeonggi";
        } else if (district.equals("Busan")) {
            for (int i = 0; i < busanDistrict.length; ++i) {
                model.addAttribute(("Busan-" + busanDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Busan-" + busanDistrict[i]));
            }
            return "map/busan";
        } else if (district.equals("Gangwon")) {
            for (int i = 0; i < gangwonDistrict.length; ++i) {
                model.addAttribute(("Gangwon-do-" + gangwonDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Gangwon-do-" + gangwonDistrict[i]));
            }
            return "map/gangwon";
        } else if (district.equals("Jeju")) {
            for (int i = 0; i < jejuDistrict.length; ++i) {
                model.addAttribute(jejuDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(jejuDistrict[i]));
            }
            return "map/jeju";
        } else if (district.equals("Ulsan")) {
            for (int i = 0; i < ulsanDistrict.length; ++i) {
                model.addAttribute(("Ulsan-" + ulsanDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Ulsan-" + ulsanDistrict[i]));
            }
            return "map/ulsan";
        } else if (district.equals("Incheon")) {
            for (int i = 0; i < incheonDistrict.length; ++i) {
                model.addAttribute(("Incheon-" + incheonDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Incheon-" + incheonDistrict[i]));
            }
            return "map/incheon";
        } else if (district.equals("Daegu")) {
            for (int i = 0; i < daeguDistrict.length; ++i) {
                model.addAttribute(("Daegu-" + daeguDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Daegu-" + daeguDistrict[i]));
            }
            return "map/daegu";
        } else if (district.equals("Daejeon")) {
            for (int i = 0; i < incheonDistrict.length; ++i) {
                model.addAttribute(("Daejeon-" + incheonDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Daejeon-" + incheonDistrict[i]));
            }
            return "map/daejeon";
        } else if (district.equals("Gwangju")) {
            for (int i = 0; i < gwangjuDistrict.length; ++i) {
                model.addAttribute(("Gwangju-" + gwangjuDistrict[i]).replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha("Gwangju-" + gwangjuDistrict[i]));
            }
            return "map/gwangju";
        }

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

        memberService.login(member);

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

    @GetMapping("/profile/{nickname}")
    public String profilePage(@PathVariable String nickname, Model model) {

        List<Mention> memberMentionList = mentionService.getMemberMentions(nickname);
        System.out.println(memberMentionList);
        Member profileMember = memberService.getNicknameMember(nickname);
        System.out.println(profileMember);
        model.addAttribute("profileMember", profileMember);
        model.addAttribute("memberMentionList", memberMentionList);


        return "co_sns/profile";
    }



    @GetMapping("/settings")
    public String setting(Model model, @AuthenticationMember Member member){
        model.addAttribute("member", memberService.getMember(member));
        return "member/settings";
    }

    @PostMapping("/settings")
    public String updateMember(Model model, @Valid SettingForm settingForm, @AuthenticationMember Member member) {

            Member updateMember = memberService.updateMember(member.getNo(), settingForm);
            model.addAttribute("updateMember",updateMember);
            model.addAttribute("result", true);

        return setting(model, member);

    }

    // ================= co_info ============================

    @GetMapping("/vaccine")
    public String vaccine(Model model) {
        VaccineCountVo vaccineCountVo = vaccineXml.getVaccineCount();
        int totalPopulation = vaccineXml.getTotalPopulation();
        List<String> cityPopulationList = vaccineXml.getCityPopulation();
        model.addAttribute("vaccineCountVo", vaccineCountVo);
        model.addAttribute("totalPopulation", totalPopulation);
        model.addAttribute("cityPopulationList", cityPopulationList);

        try {
            model.addAttribute("articleList",articleService.getVaccineArticleList());
        }catch (IOException  e){
            e.printStackTrace();
        }

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

    @RequestMapping("/article")
    public String article(Model model) {

        try {
            model.addAttribute("articleList",articleService.getLocalArticleList("서울", "전체"));
        }catch (IOException  e){
            e.printStackTrace();
        }

        return "co_info/article";
    }

    @GetMapping("/local_article")
    public String article(@RequestParam String mainCityName, @RequestParam String subCityName, Model model){

        try {
            model.addAttribute("articleList",articleService.getLocalArticleList(mainCityName, subCityName));
        }catch (IOException e){
            e.printStackTrace();
            // return "점검 페이지"
        }

        return "co_info/article :: #article-list";
    }



    // ================= co_sns ============================

    //타임라인(팔로우)
    @GetMapping("/timeline_follow")
    public String timelineFollow(Model model){

        List<Mention> mentionFormList = mentionService.getMentionlist();

        model.addAttribute("member", memberService);
        model.addAttribute("mention",mentionService);
        model.addAttribute("mentionFormList", mentionFormList);
        model.addAttribute("mentionForm", new MentionForm());

        return "co_sns/timeline_follow";
    }

    //타임라인(위치)
    @GetMapping("/timeline_location")
    public String timelineLocation(Model model)
    {
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
    public String mentionDetail(@PathVariable Long no, Model model, @AuthenticationMember Member member){
        try {
            Mention parentMention = mentionService.getMention(no);
            model.addAttribute("mention", parentMention );
            model.addAttribute("reMentionForm", new ReMentionForm());
            model.addAttribute("reMentionList",reMentionService.getReMentionList(parentMention));
            model.addAttribute("currentMember", memberService.getMember(member));
            
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
            model.addAttribute("member", memberService.getMember(member));

        }catch (IllegalArgumentException e){
            return "co_sns/timeline_location";
        }

        return "co_sns/mention_detail :: #re-mention-list";
    }


    @GetMapping("/search")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<Mention> mentionFormList = mentionService.searchMentions(keyword);

        model.addAttribute("member", memberService);
        model.addAttribute("mention",mentionService);
        model.addAttribute("mentionFormList", mentionFormList);
        model.addAttribute("mentionForm", new MentionForm());

        return "co_sns/timeline_follow";

    }


    @PostMapping("/delete/{no}")
    public String delete(@PathVariable Long no,Model model){

        mentionService.deleteMention(no);
        model.addAttribute("result", true);

        return "redirect:/timeline_follow";
    }



}
