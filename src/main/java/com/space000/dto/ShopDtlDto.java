package com.space000.dto;

import com.space000.entity.Review;
import com.space000.entity.Shop;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShopDtlDto {
    private String category; //분류
    private String name; //상호명
    private String roadName; //도로명
    private Byte rating; //별점

    private List<ShopReviewDto> shopReviewDtoList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public static ShopDtlDto of(Shop shop) {
        ShopDtlDto shopDtlDto = modelMapper.map(shop, ShopDtlDto.class);

        int sum = 0;

        if (!shop.getReviewList().isEmpty()){
            for (Review review : shop.getReviewList()) {
                shopDtlDto.getShopReviewDtoList().add(ShopReviewDto.of(review));
                sum += (int)review.getRating();
            }

            shopDtlDto.setRating((byte)(sum/shop.getReviewList().size()));
        }
        return shopDtlDto;
    }

}
