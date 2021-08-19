package com.megait.nocoronazone.controller;

import com.megait.nocoronazone.api.VaccineCountVo;
import com.megait.nocoronazone.api.VaccineXml;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final VaccineXml vaccineXml;

    @RequestMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "member/login";
    }


    @GetMapping("/cosns")
    public String timelineFollow(){
        return "co_sns/timeline_follow";
    }

    @GetMapping("/timeline_location")
    public String timelineLocation(){
        return "co_sns/timeline_location";
    }

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
    public String news() {
        return "co_info/news";
    }


    @GetMapping("/article")
    public String article() {
        return "co_info/article";
    }


}
