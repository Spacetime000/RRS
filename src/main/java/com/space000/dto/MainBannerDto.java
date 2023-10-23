package com.space000.dto;

import com.space000.entity.Banner;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class MainBannerDto {
    private String fileUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static MainBannerDto of(Banner banner) {
        return modelMapper.map(banner, MainBannerDto.class);
    }
}
