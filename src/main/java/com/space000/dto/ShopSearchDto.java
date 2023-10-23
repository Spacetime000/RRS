package com.space000.dto;

import lombok.Data;

@Data
public class ShopSearchDto {

    private String searchCategory; //전체, 각 분류별
    private String searchBy; //상호명, 주소
    private String searchQuery = ""; //검색어
}
