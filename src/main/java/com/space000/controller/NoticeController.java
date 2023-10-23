package com.space000.controller;

import com.space000.dto.NoticeBoardDto;
import com.space000.dto.NoticeFormDto;
import com.space000.service.MainService;
import com.space000.service.NoticeFileService;
import com.space000.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;
    private final NoticeFileService noticeFileService;


    /**
     * 공지사항 작성 페이지 요청
     * @return 공지사항 작성 페이지(Form) 요청
     */
    @GetMapping(value = "/new")
    public String form(Model model) {
        model.addAttribute("noticeFormDto", new NoticeFormDto());
        return "notice/noticeForm";
    }

    /**
     * 공지사항 작성 저장
     * @return - 정상 처리시 공지사항 보드 페이지
     */
    @PostMapping(value = "/new")
    public String saveNotice(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model,
                             @RequestParam("file") List<MultipartFile> multipartFileList) {

        if (bindingResult.hasErrors()) {
            log.error("공지사항 에러 : {}", bindingResult.getAllErrors());
            return "notice/noticeForm";
        }

        try {
            Long noticeId = noticeService.saveNotice(noticeFormDto, multipartFileList);
            return "redirect:/notice/dtl/" + noticeId;
        } catch (Exception e) {
            model.addAttribute("error", "에러");
            return "notice/noticeForm";
        }

    }

    @GetMapping(value = "/dtl/{noticeId}")
    public String noticeDtl(@PathVariable("noticeId") Long noticeId, Model model) {

        try {
            NoticeFormDto noticeFormDto = noticeService.getNoticeFormDto(noticeId);
            model.addAttribute("noticeFormDto", noticeFormDto);
        } catch (EntityNotFoundException e) {
            log.error("해당 공지글이 없습니다. {}", e.getMessage());
            return "redirect:/notice/board";
        } catch (Exception e) {
            log.error("에러 발생 {}", e.getMessage());
            return "redirect:/notice/board";
        }

        return "notice/noticeDtl";
    }

    //공지사항 보드
    @GetMapping(value = "/board")
    public String board(Model model) {
        List<NoticeBoardDto> noticeBoardDtoList = noticeService.getNoticeBoardDto();
        model.addAttribute("noticeBoardDtoList", noticeBoardDtoList);
        return "notice/noticeBoard";
    }

    @GetMapping(value = "/update/{noticeId}")
    public String updateNoticeForm(@PathVariable("noticeId") Long noticeId, Model model) {

        try {
            NoticeFormDto noticeFormDto = noticeService.getNoticeFormDto(noticeId);
            model.addAttribute("noticeFormDto", noticeFormDto);
        } catch (EntityNotFoundException e) {
            log.error("해당 게시글이 없습니다. : {}", e.getMessage());
            model.addAttribute("error", "해당 게시글이 없습니다.");
            return "redirect:/notice/board";
        }

        return "notice/noticeForm";
    }

    @PostMapping(value = "/update/{noticeId}")
    public String updateNotice(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult,
                               @RequestParam("file") List<MultipartFile> multipartFileList) {

        if (bindingResult.hasErrors()) {
            log.error("공지사항 수정 에러 : {}", bindingResult.getAllErrors());
            return "notice/update/" + noticeFormDto.getId();
        }

        try {
            noticeService.updateNotice(noticeFormDto, multipartFileList);
        } catch (Exception e) {
            log.error("에러 발생 : {}", e.getMessage());
            return "notice/noticeForm";
        }
        log.info("ID 체크 : {}", noticeFormDto.getId());
        return "redirect:/notice/dtl/" + noticeFormDto.getId();
    }

    @DeleteMapping(value = "/delete/{noticeId}")
    public ResponseEntity deleteNotice(@PathVariable("noticeId") Long noticeId) throws Exception {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }

    /**
     * 첨부파일 다운로드
     */
    @GetMapping(value = "/fileDown/{fileId}")
    public ResponseEntity<Resource> fileDown(@PathVariable("fileId") Long fileId) throws MalformedURLException {
        return noticeFileService.downFile(fileId);
    }

    @PostMapping(value = "/fileDelete/{fileId}")
    public ResponseEntity fileDelete(@PathVariable("fileId") Long fileId) {
        try {
            noticeFileService.deleteNoticeFile(fileId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
