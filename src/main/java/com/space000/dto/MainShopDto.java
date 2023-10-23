package com.space000.dto;

import com.space000.entity.Review;
import com.space000.entity.Shop;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Random;

@Data
public class MainShopDto {
    private Long id;
    private String category;
    private String roadName;
    private String name;
    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static MainShopDto of(Shop shop) {
        MainShopDto mainShopDto = modelMapper.map(shop, MainShopDto.class);
        List<Review> reviewList = shop.getReviewList();

        Random rd = new Random();

        if (reviewList.size() == 0) {
            mainShopDto.imgUrl = "/img/null.png";
        } else {
            mainShopDto.imgUrl = reviewList.get(rd.nextInt(reviewList.size())).getReviewFileList().get(0).getFileUrl();
        }
        return mainShopDto;
    }
}
