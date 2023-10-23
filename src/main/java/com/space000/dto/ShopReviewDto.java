package com.space000.dto;

import com.space000.entity.Review;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Data
public class ShopReviewDto {
    private Long id;
    private String thumbnail;
    private String title;
    private Byte rating;
    private String createBy;
    private LocalDateTime createTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ShopReviewDto of(Review review) {
        ShopReviewDto shopReviewDto = modelMapper.map(review, ShopReviewDto.class);
        shopReviewDto.setCreateBy(review.getMember().getNickname());
        shopReviewDto.setThumbnail(review.getReviewFileList().get(0).getFileUrl());
        return shopReviewDto;
    }
}
