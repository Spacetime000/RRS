package com.space000.service;

import com.space000.dto.MainBannerDto;
import com.space000.dto.MainShopDto;
import com.space000.dto.ShopBoardDto;
import com.space000.entity.Banner;
import com.space000.entity.Member;
import com.space000.entity.Shop;
import com.space000.repository.BannerRepository;
import com.space000.repository.MemberRepository;
import com.space000.repository.ReviewRepository;
import com.space000.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MainService {
    private final MemberRepository memberRepository;
    private final BannerRepository bannerRepository;
    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 헤더 부분 프로필 이미지
     * @param email
     * @return - 이미지 경로
     */
    @Transactional(readOnly = true)
    public String getInfoImg(String email) {
        Member member = memberRepository.findByEmail(email);
        String url = member.getMemberImg().getFileUrl();
        return url;
    }

    @Transactional(readOnly = true)
    public List<MainBannerDto> getMainBanner() {
        List<Banner> mainBanner = bannerRepository.getMainBanner();
        List<MainBannerDto> mainBannerDtoList = new ArrayList<>();

        for (Banner banner : mainBanner) {
            mainBannerDtoList.add(MainBannerDto.of(banner));
        }

        return mainBannerDtoList;
    }

    /**
     * 메인 페이지 추천 리스트
     */
    @Transactional(readOnly = true)
    public List<MainShopDto> getMainShopList() {
        List<Shop> bestShop = shopRepository.getBestShop();
        List<MainShopDto> mainShopDtoList = new ArrayList<>();

        for (Shop shop : bestShop) {
            mainShopDtoList.add(MainShopDto.of(shop));
        }

        return mainShopDtoList;
    }

}
