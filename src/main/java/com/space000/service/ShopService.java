package com.space000.service;

import com.space000.dto.ShopBoardDto;
import com.space000.dto.ShopDtlDto;
import com.space000.dto.ShopSearchDto;
import com.space000.entity.Shop;
import com.space000.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ShopService {

    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    public ShopDtlDto getShopDtlDto(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return ShopDtlDto.of(shop);
    }

    @Transactional(readOnly = true)
    public Page<ShopBoardDto> getShopBoard(int page, ShopSearchDto shopSearchDto) {
        PageRequest pageRequest = PageRequest.of(page, 9);
        Page<Shop> pageShop = shopRepository.getShopBoardPage(shopSearchDto, pageRequest);
        Page<ShopBoardDto> pageDto = pageShop.map(shop -> new ShopBoardDto(
                shop.getId(), shop.getCategory(), shop.getName(), shop.getRoadName(), shop.getReviewList()));

        return pageDto;
    }

    //중복이 없는 카테고리 목록
    @Transactional(readOnly = true)
    public List<String> getCategory() {
        return shopRepository.getCategory();
    }

}
