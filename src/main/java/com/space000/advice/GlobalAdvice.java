package com.space000.advice;

import com.space000.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalAdvice {
    private final MainService mainService;

    @ModelAttribute("headerMemberImg")
    public String memberImg(Principal principal) {
        try {
            return mainService.getInfoImg(principal.getName());
        } catch (Exception e) {
            return ""; //비회원 일 경우 보이지 않음.
        }
    }
}
