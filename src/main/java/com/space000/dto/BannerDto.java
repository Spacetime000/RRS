package com.space000.dto;

import com.space000.entity.Banner;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class BannerDto {
    private Long id;
    private String oriFileName;
    private String fileUrl;
    private String state;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BannerDto of(Banner banner) {
        return modelMapper.map(banner, BannerDto.class);
    }
}
