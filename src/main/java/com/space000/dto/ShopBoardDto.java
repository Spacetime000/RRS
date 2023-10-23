package com.space000.dto;

import com.space000.entity.Review;
import com.space000.entity.Shop;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Random;

@Data
public class ShopBoardDto {
    private Long id;
    private String category;
    private String shopName;
    private String roadName;
    private Byte rating; //평균별점
    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public ShopBoardDto(Long id, String category, String shopName, String address, List<Review> reviewList) {
        Random rd = new Random();

        this.id = id;
        this.category = category;
        this.shopName = shopName;
        this.roadName = address;

        long sum = 0;

        for (Review review : reviewList) {
            sum += (long)review.getRating();
        }

        if (reviewList.size() == 0) {
            this.rating = 0;
            this.imgUrl = "/img/null.png";
        } else {
            this.rating = (byte)Math.abs(sum/reviewList.size());
            this.imgUrl = reviewList.get(rd.nextInt(reviewList.size())).getReviewFileList().get(0).getFileUrl();
        }
    }

}
