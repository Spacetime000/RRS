package com.space000.controller;

import com.space000.dto.ShopBoardDto;
import com.space000.dto.ShopDtlDto;
import com.space000.dto.ShopSearchDto;
import com.space000.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shop")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/dtl/{shopId}")
    public String shopDtl(@PathVariable("shopId") Long shopId, Model model) {
        ShopDtlDto shopDtlDto = shopService.getShopDtlDto(shopId);
        model.addAttribute("shop", shopDtlDto);
        return "shop/shopDtl";
    }

    @GetMapping("/board")
    public String board(ShopSearchDto shopSearchDto, @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Page<ShopBoardDto> shopPage = shopService.getShopBoard(page, shopSearchDto);
        model.addAttribute("pages", shopPage);
        model.addAttribute("maxPage", 10);
        model.addAttribute("categoryList", shopService.getCategory());
        model.addAttribute("shopSearchDto", shopSearchDto);
        return "shop/shopBoard";
    }
}
