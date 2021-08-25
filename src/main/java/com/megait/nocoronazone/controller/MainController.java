package com.megait.nocoronazone.controller;

import com.google.gson.JsonObject;
import com.megait.nocoronazone.api.VaccineCountVo;
import com.megait.nocoronazone.api.VaccineXml;
import com.megait.nocoronazone.domain.ChatMessage;
import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.SafetyIndex;
import com.megait.nocoronazone.dto.MentionDto;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.service.DetailSafetyService;
import com.megait.nocoronazone.service.MemberService;
import com.megait.nocoronazone.service.MentionService;
import com.megait.nocoronazone.service.SafetyService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final DetailSafetyService detailSafetyService;
    private final SafetyService safetyService;
    private final VaccineXml vaccineXml;

    private final MemberService memberService;
    private final MentionService mentionService;

    String colorConfirmed = "235, 64, 52"; // red
    String colorDensity = "158, 0, 158"; // purple

    // 전체
    String[] City = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};
    String[] City2 = {"Seoul", "Busan", "Daegu", "Incheon", "Gwangju", "Daejeon", "Ulsan", "Gyeonggi", "Gangwon", "Chungbuk", "Chungnam", "Jeonbuk", "Jeonnam", "Gyeongbuk", "Gyeongnam", "Jeju", "Sejong"};
    // 충청북도
    String[] cbDistrict = {"Boeun-gun", "Cheongju-si", "Chungju-si", "Danyang-gun", "Goesan-gun", "Jecheon-si", "Jeungpyeong-gun", "Jincheon-gun", "Okcheon-gun", "Eumseong-gun", "Yeongdong-gun"};
    // 충청남도
    String[] cnDistrict = {"Dangjin-si", "Cheongju-si", "Seosan-si", "Nonsan-si", "Cheonan-si", "Gongju-si", "Boryeong-si", "Asan-si", "Gyeryong-si", "Geumsan-gun", "Buyeo-gun", "Seocheon-gun", "Cheongyang-gun", "Hongseong-gun", "Yesan-gun", "Taean-gun", "Eumseong-gun"};
    // 경상북도
    String[] gbDistrict = {"Pohang-si", "Gyeongju-si", "Gimcheon-si", "Andong-si", "Gumi-si", "Yeongju-si", "Yeongcheon-si", "Sangju-si", "Mungyeong-si", "Gyeongsan-si", "Gunwi-gun", "Uiseong-gun", "Cheongsong-gun", "Yeongyang-gun", "Yeongdeok-gun", "Cheongdo-gun", "Goryeong-gun", "Seongju-gun", "Chilgok-gun", "Yecheon-gun", "Bonghwa-gun", "Uljin-gun", "Ulleung-gun"};
    // 경상남도
    String[] gnDistrict = {"Changwon-si", "Jinju-si", "Tongyeong-si", "Sacheon-si", "Gimhae-si", "Miryang-si", "Geoje-si", "Yangsan-si", "Uiryeong-gun", "Haman-gun", "Changnyeong-gun", "Goseong-gun", "Namhae-gun", "Hadong-gun", "Sancheong-gun", "Hamyang-gun", "Geochang-gun", "Hapcheon-gun"};
    // 전라북도
    String[] jbDistrict = {"Jeonju-si", "Gunsan-si", "Iksan-si", "Jeongeup-si", "Namwon-si", "Gimje-si", "Wanju-gun", "Jinan-gun", "Muju-gun", "Jangsu-gun", "Imsil-gun", "Sunchang-gun", "Gochang-gun", "Buan-gun"};
    // 전라남도
    String[] jnDistrict = {"Mokpo-si", "Yeosu-si", "Suncheon-si", "Naju-si", "Gwangyang-si", "Damyang-gun", "Gokseong-gun", "Gurye-gun", "Goheung-gun", "Boseong-gun", "Hwasun-gun", "Jangheung-gun", "Gangjin-gun", "Haenam-gun", "Yeongam-gun", "Muan-gun", "Hampyeong-gun", "Yeonggwang-gun", "Jangseong-gun", "Wando-gun", "Jindo-gun", "Sinan-gun"};
    // 경기
    String[] ggDistrict = {"Suwon-si", "Seongnam-si", "Uijeongbu-si", "Anyang-si", "Bucheon-si", "Gwangmyeong-si", "Pyeongtaek-si", "Dongducheon-si", "Ansan-si", "Goyang-si", "Gwacheon-si", "Guri-si", "Namyangju-si", "Osan-si", "Siheung-si", "Gunpo-si", "Uiwang-si", "Hanam-si", "Yongin-si", "Paju-si", "Icheon-si", "Anseong-si", "Gimpo-si", "Hwaseong-si", "Gwangju-si", "Yangju-si", "Pocheon-si", "Yeoju-si", "Yeoncheon-gun", "Gapyeong-gun", "Yangpyeong-gun", "Sinan_gun"};
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


    // ================= 메인 ============================
    @RequestMapping("/")
    public String index(Model model) {
        List<SafetyIndex> safetyList = safetyService.getSafetyList();
        model.addAttribute("safetyList", safetyList);
        model.addAttribute("color", colorConfirmed);
        for(int i = 0; i < City.length; ++i){
            model.addAttribute(City2[i], safetyService.getConfirmedtoAlpha(City[i]));
        }
        return "index";
    }

    @GetMapping("/infection")
    public String infection(Model model) {
        List<SafetyIndex> safetyList = safetyService.getSafetyList();
        model.addAttribute("safetyList", safetyList);
        model.addAttribute("color", colorConfirmed);
        for(int i = 0; i < City.length; ++i){
            model.addAttribute(City2[i], safetyService.getConfirmedtoAlpha(City[i]));
        }
        return "index";
    }

    @GetMapping("/density")
    public String density(Model model) {
        for(int i = 0; i < City.length; ++i){
            model.addAttribute(City2[i], safetyService.getSafetytoAlpha(City[i]));
        }
        model.addAttribute("color", colorDensity);
        return "index";
    }

    @GetMapping("/detail")
    public String detail(Model model, @Param(value = "district")String district) {
        model.addAttribute("color", colorDensity);
        if (district.equals("Seoul")){
            for(int i = 0; i < seoulDistrict.length; ++i){
                model.addAttribute(seoulDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(seoulDistrict[i]));
            }
            return "map/seoul";
        } else if (district.equals("Chungbuk")) {
            for (int i = 0; i < cbDistrict.length; ++i) {
                model.addAttribute(cbDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(cbDistrict[i]));
            }
            return "map/chungbuk";
        } else if (district.equals("Chungnam")) {
            for (int i = 0; i < cnDistrict.length; ++i) {
                model.addAttribute(cnDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(cnDistrict[i]));
            }
            return "map/chungnam";
        } else if (district.equals("Gyeongbuk")) {
            for (int i = 0; i < gbDistrict.length; ++i) {
                model.addAttribute(gbDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(gbDistrict[i]));
            }
            return "map/gyeongbuk";
        } else if (district.equals("Gyeongnam")) {
            for (int i = 0; i < gnDistrict.length; ++i) {
                model.addAttribute(gnDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(gnDistrict[i]));
            }
            return "map/gyeongnam";
        } else if (district.equals("Jeonbuk")) {
            for (int i = 0; i < jbDistrict.length; ++i) {
                model.addAttribute(jbDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(jbDistrict[i]));
            }
            return "map/jeonbuk";
        } else if (district.equals("Jeonnam")) {
            for (int i = 0; i < jnDistrict.length; ++i) {
                model.addAttribute(jnDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(jnDistrict[i]));
            }
            return "map/jeonnam";
        } else if (district.equals("Gyeonggi")) {
            for (int i = 0; i < ggDistrict.length; ++i) {
                model.addAttribute(ggDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(ggDistrict[i]));
            }
            return "map/gyeonggi";
        } else if (district.equals("Busan")) {
            for (int i = 0; i < busanDistrict.length; ++i) {
                model.addAttribute(busanDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(busanDistrict[i]));
            }
            return "map/busan";
        } else if (district.equals("Gangwon")) {
            for (int i = 0; i < gangwonDistrict.length; ++i) {
                model.addAttribute(gangwonDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(gangwonDistrict[i]));
            }
            return "map/gangwon";
        } else if (district.equals("Jeju")) {
            for (int i = 0; i < jejuDistrict.length; ++i) {
                model.addAttribute(jejuDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(jejuDistrict[i]));
            }
            return "map/jeju";
        } else if (district.equals("Ulsan")) {
            for (int i = 0; i < ulsanDistrict.length; ++i) {
                model.addAttribute(ulsanDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(ulsanDistrict[i]));
            }
            return "map/Ulsan";
        } else if (district.equals("Incheon")) {
            for (int i = 0; i < incheonDistrict.length; ++i) {
                model.addAttribute(incheonDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(incheonDistrict[i]));
            }
            return "map/incheon";
        } else if (district.equals("Daegu")) {
            for (int i = 0; i < incheonDistrict.length; ++i) {
                model.addAttribute(incheonDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(incheonDistrict[i]));
            }
            return "map/daegu";
        } else if (district.equals("Daejeon")) {
            for (int i = 0; i < incheonDistrict.length; ++i) {
                model.addAttribute(incheonDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(incheonDistrict[i]));
            }
            return "map/daejeon";
        } else if (district.equals("Gwangju")) {
            for (int i = 0; i < incheonDistrict.length; ++i) {
                model.addAttribute(incheonDistrict[i].replace("-", "_"), detailSafetyService.getDetailSafetytoAlpha(incheonDistrict[i]));
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
    public String list(Model model) {
        List<MentionDto> mentionList = mentionService.getMentionlist();

        model.addAttribute("mentionList", mentionList);
        return "co_sns/timeline_follow";
    }

    //타임라인(위치)
    @GetMapping("/timeline_location")
    public String timelineLocation(){
        return "co_sns/timeline_location";
    }


    // 게시글 쓰기
    @PostMapping("/mention")
    public String write(MentionDto mentionDto) {
        System.out.println("넘어오니?");
        mentionService.savePost(mentionDto);

        return "co_sns/timeline_follow";
    }

    @GetMapping("/mention_detail")
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
