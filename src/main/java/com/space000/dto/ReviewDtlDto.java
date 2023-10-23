package com.space000.dto;

import com.space000.entity.Review;
import com.space000.entity.ReviewFile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class ReviewDtlDto {

    private Long id; //다른 버튼 경로 설정용
    private String title;
    private String content;
    private Byte rating;
    private String createBy; //글 작성자 확인 용도
    private LocalDateTime createTime;

    private String category;
    private String nickname;
    private String profileImgUrl;
    private String shopName;
    private List<String> fileList = new ArrayList<>(); //dtl 수정 완료 후 제거 예정
    private List<String> fileUrl = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewDtlDto of(Review review) {
        ReviewDtlDto reviewDtlDto = modelMapper.map(review, ReviewDtlDto.class);
        reviewDtlDto.setCategory(review.getShop().getCategory());
        reviewDtlDto.setNickname(review.getMember().getNickname());
        reviewDtlDto.setProfileImgUrl(review.getMember().getMemberImg().getFileUrl());
        reviewDtlDto.setShopName(review.getShop().getName());

        for (ReviewFile reviewFile : review.getReviewFileList()) {
            reviewDtlDto.getFileUrl().add(reviewFile.getFileUrl());
        }
        return reviewDtlDto;
    }

}
