package com.space000.controller;

import com.space000.dto.MyInfoDto;
import com.space000.dto.RegisterChkDto;
import com.space000.dto.RegisterDto;
import com.space000.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Locale;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MessageSource messageSource;

    @GetMapping(value = "/login")
    public String loginPage() {
        log.info("로그인 페이지 호출");
        return "members/login";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model) {
        log.info("회원가입 페이지 호출");
        model.addAttribute("registerDto", new RegisterDto());
        return "members/register";
    }

    //회원가입 요청
    @PostMapping(value = "/register")
    public String register(@Valid RegisterDto registerDto, BindingResult bindingResult, Model model,
                           @RequestHeader(value = "Accept-Language") String acceptLanguage) {

        String lang = acceptLanguage.split(",")[0];

        if (bindingResult.hasErrors()) {
            log.info("오류 내용 : {}", bindingResult.getAllErrors());
            return "members/register";
        }

        try {
            memberService.saveMember(registerDto);
        } catch(DataIntegrityViolationException e) { //중복 가입
            if (lang.equals("ko")) //에러 메시지
                model.addAttribute("error", messageSource.getMessage("Error.register.duplicate_registration", null, null));
            else
                model.addAttribute("error", messageSource.getMessage("Error.register.duplicate_registration", null, Locale.ENGLISH));
            return "members/register";
        } catch(Exception e) {
            model.addAttribute("error", "error");
        }

        log.info("회원가입 성공");
        return "redirect:/members/login";
    }

    //로그인 실패
    @GetMapping(value = "/login/error")
    public String loginError(Model model, @RequestHeader(value = "Accept-Language") String acceptLanguage) {
        log.info("로그인 실패");
        String lang = acceptLanguage.split(",")[0];

        if (lang.equals("ko"))
            model.addAttribute("loginError", messageSource.getMessage("Error.login", null, null));
        else
            model.addAttribute("loginError", messageSource.getMessage("Error.login", null, Locale.ENGLISH));
        return "/members/login";
    }

    //중복 검사
    @PostMapping(value = "/vd")
    @ResponseBody
    public ResponseEntity<Boolean> validateDuplicate(@RequestBody RegisterChkDto registerChkDto) {

        try {
            memberService.validateDuplicate(registerChkDto.getSource(), registerChkDto.getKey());
            return ResponseEntity.ok(Boolean.TRUE);
        } catch (Exception e) {
            log.info("중복 검사 예외 메시지 확인 : {}", e.getMessage());
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }

    //내 정보 페이지
    @GetMapping(value = "/myInfo")
    public String myInfoPage(Model model, Principal principal) {
        MyInfoDto myInfo = memberService.getMyinfo(principal.getName());

        model.addAttribute("myInfo", myInfo);
        return "members/myInfo";
    }

    @PostMapping(value = "/myInfo")
    public String updateMyInfo(MyInfoDto myInfoDto, @RequestParam("file") MultipartFile multipartFile) throws Exception {

        if (multipartFile.isEmpty()) {
            log.info("업로드 이미지가 없습니다.");
            return "redirect:/members/myInfo";
        }

        memberService.updateMyinfo(myInfoDto.getId(), multipartFile);

        return "redirect:/members/myInfo";

    }
}
