package com.space000.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewBoardDto {
    private Long id; //페이지 이동을 위한
    private String category; //분류
    private String shopName; //상호명
    private String title; //제목
    private Byte rating; //별점
    private String createBy; //작성자
    private LocalDateTime createTime; //작성날짜


    public ReviewBoardDto(Long id, String category, String shopName, String title, Byte rating, String createBy, LocalDateTime createTime) {
        this.id = id;
        this.category = category;
        this.shopName = shopName;
        this.title = title;
        this.rating = rating;
        this.createBy = createBy;
        this.createTime = createTime;
    }
}
