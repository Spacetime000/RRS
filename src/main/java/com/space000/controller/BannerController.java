package com.space000.controller;

import com.space000.dto.BannerDto;
import com.space000.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/banner")
@Slf4j
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/new")
    public String newBanner() {
        return "banner/bannerForm";
    }

    @PostMapping("/new")
    public String saveBanner(@RequestParam String state, @RequestParam("file") MultipartFile multipartFile, Model model) throws IOException {

        if (multipartFile.isEmpty()) {
            model.addAttribute("error", "업로드할 파일이 없습니다.");
            return "banner/bannerForm";
        }

        bannerService.saveBanner(state, multipartFile);
        return "redirect:/banner/board";
    }

    @GetMapping("/update/{id}")
    public String updateBanner(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "banner/bannerForm";
    }

    @PostMapping("/update")
    public String updatedBanner(@RequestParam String state, @RequestParam("id") Long id, @RequestParam("file") MultipartFile multipartFile, Model model) {
        try {
            if (multipartFile.isEmpty()) {
                bannerService.updateBannerState(state, id);
                return "redirect:/banner/board";
            }

            String fileName = multipartFile.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

            if (!ext.equalsIgnoreCase("jpg") && !ext.equalsIgnoreCase("png")
                    && !ext.equalsIgnoreCase("gif")) {
                log.error("첨부파일이 이미지 파일 형식이 아닙니다.");
                model.addAttribute("error", "이미지 파일 형식이 아닙니다.");
                model.addAttribute("id", id);
                return "banner/bannerForm";
            } else {
                bannerService.updateBanner(state, id, multipartFile);
            }
        } catch (Exception e) {
            model.addAttribute("error", "에러 발생");
        }

        return "redirect:/banner/board";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBanner(@PathVariable("id") Long id) {
        try {
            bannerService.deleteBanner(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/board")
    public String board(Model model) {
        List<BannerDto> bannerDtoList = bannerService.getBannerManagement();

        model.addAttribute("bannerList", bannerDtoList);
        return "banner/bannerBoard";
    }
}
