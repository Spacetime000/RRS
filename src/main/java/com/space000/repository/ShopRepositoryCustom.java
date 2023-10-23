package com.space000.repository;

import com.space000.dto.ShopSearchDto;
import com.space000.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopRepositoryCustom {
    Page<Shop> getShopBoardPage(ShopSearchDto shopSearchDto, Pageable pageable);

    List<Shop> getBestShop();
}
