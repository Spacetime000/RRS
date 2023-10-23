package com.space000.controller;

import com.space000.dto.ReviewBoardDto;
import com.space000.dto.ReviewFormDto;
import com.space000.service.MainService;
import com.space000.service.ReviewFileService;
import com.space000.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewFileService reviewFileService;

    private final MainService mainService;

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("reviewFormDto", new ReviewFormDto());
        return "review/reviewForm";
    }

    @PostMapping("/new")
    public String save(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult,
                       @RequestParam("file") List<MultipartFile> multipartFileList, Model model) {

        if (bindingResult.hasErrors()) {
            log.info("리뷰 저장 에러 체크 : {}", bindingResult.getAllErrors());
            return "review/reviewForm";
        }

        if (multipartFileList.get(0).isEmpty()) {
            log.info("리뷰 이미지가 하나도 없음.");
            model.addAttribute("error", "이미지 하나 이상 필수입니다.");
            return "review/reviewForm";
        }

       //이미지 확장자 체크
        for (MultipartFile multipartFile : multipartFileList) {
            String filename = multipartFile.getOriginalFilename();
            String ext = filename.substring(filename.lastIndexOf(".") + 1);

            if (!ext.equalsIgnoreCase("jpg") && !ext.equalsIgnoreCase("png")
                    && !ext.equalsIgnoreCase("gif")) {
                log.error("첨부파일이 이미지 파일 형식이 아닙니다.");
                model.addAttribute("error", "첨부파일이 없거나 첨부파일 중에 이미지 파일 형식이 아닌것이 있습니다.");
                return "review/reviewForm";
            }
        }

        Long reviewId;

        try {
            reviewId = reviewService.saveReview(reviewFormDto, multipartFileList);
        } catch (Exception e) {
            model.addAttribute("error", "에러");
            log.error("예외 발생 : {}", e.getMessage());
            return "review/reviewForm";
        }
        return "redirect:/review/dtl/" + reviewId;
    }

    @GetMapping("/dtl/{reviewId}")
    public String reviewDtl(@PathVariable("reviewId") Long reviewId, Model model) {
        model.addAttribute("reviewDtlDto", reviewService.getReviewDtl(reviewId));
        return "review/reviewDtl";
    }

    @GetMapping("/update/{reviewId}")
    public String updateReview(@PathVariable("reviewId") Long reviewId, Model model, Principal principal) {
        ReviewFormDto reviewFormDto = reviewService.getReviewFormDto(reviewId, principal.getName());
        model.addAttribute("reviewFormDto", reviewFormDto);
        return "review/reviewForm";
    }

    @PostMapping("/update/{reviewId}")
    public String updateReview(@Valid ReviewFormDto reviewFormDto, BindingResult bindingResult,
                               @RequestParam("file") List<MultipartFile> multipartFileList, Principal principal,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "/review/update/" + reviewFormDto.getId();
        }

        try {
            reviewService.updateReview(reviewFormDto, multipartFileList, principal.getName());
        } catch (Exception e) {
            return "/review/update/" + reviewFormDto.getId();
        }

        return "redirect:/review/dtl/" + reviewFormDto.getId();

    }

    @DeleteMapping(value = "/delete/{fileId}")
    public ResponseEntity deleteFile(@PathVariable("fileId") Long fileId, Principal principal) {
        try {
            reviewFileService.deleteReviewFile(fileId, principal.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    //리뷰 삭제
    @DeleteMapping(value = "/del/{reviewId}")
    public String deleteReview(@PathVariable("reviewId") Long reviewId, Principal principal) {
        try {
            reviewService.deleteReview(principal.getName(), reviewId);
        } catch (Exception e) {
            log.info("리뷰 삭제 예외 메시지 : {}", e.getMessage());
            return "redirect:/review/board";
        }
        return "redirect:/review/board";
    }

    @GetMapping("/board")
    public String board(Principal principal, Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<ReviewBoardDto> reviewPage = reviewService.getReviewPage(page);
        model.addAttribute("pages", reviewPage);
        model.addAttribute("maxPage", 10);

        return "review/reviewBoard";
    }
}
