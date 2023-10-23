package com.space000.dto;

import com.space000.entity.ReviewFile;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

@Data
public class ReviewFileDto {

    private Long id;
    private String fileName;

    @NotBlank
    private String oriFileName;
    private String fileUrl;
    private static ModelMapper modelMapper = new ModelMapper();

    public static ReviewFileDto of(ReviewFile reviewFile) {
        return modelMapper.map(reviewFile, ReviewFileDto.class);
    }

}
