package com.space000.dto;

import com.space000.entity.Review;
import com.space000.entity.ReviewFile;
import com.space000.entity.Shop;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class ReviewFormDto {

    private static ModelMapper modelMapper = new ModelMapper();

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Byte rating;

    @NotNull
    private Long shopId;

    @NotBlank
    private String shopName;

    @NotBlank
    private String roadName;

    @NotBlank
    private String category;

    private List<ReviewFileDto> reviewFileDtoList = new ArrayList<>();

    public Shop createShop() {
        Shop shop = new Shop();
        shop.setId(this.shopId);
        shop.setName(this.shopName);
        shop.setCategory(this.category);
        shop.setRoadName(this.roadName);
        return shop;
    }

    public Review createReview() {
        return modelMapper.map(this, Review.class);
    }

    public static ReviewFormDto of(Review review) {
        ReviewFormDto reviewFormDto = modelMapper.map(review, ReviewFormDto.class);

        reviewFormDto.setShopId(review.getShop().getId());
        reviewFormDto.setShopName(review.getShop().getName());
        reviewFormDto.setRoadName(review.getShop().getRoadName());
        reviewFormDto.setCategory(review.getShop().getCategory());


        for (int i = 0; i < review.getReviewFileList().size(); i++) {
            reviewFormDto.getReviewFileDtoList().add(ReviewFileDto.of(review.getReviewFileList().get(i)));
        }

        return reviewFormDto;
    }

}
