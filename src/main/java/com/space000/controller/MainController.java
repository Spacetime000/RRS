package com.space000.controller;

import com.space000.dto.MainBannerDto;
import com.space000.dto.MainShopDto;
import com.space000.service.BannerService;
import com.space000.service.MainService;
import com.space000.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final MainService mainService;

    @GetMapping("/")
    public String index(Model model) {
        List<MainBannerDto> mainBanner = mainService.getMainBanner();
        List<MainShopDto> mainShopList = mainService.getMainShopList();
        model.addAttribute("bannerList", mainBanner);
        model.addAttribute("mainShopList", mainShopList);
        log.info("메인 페이지 호출");
        return "index";
    }

}
