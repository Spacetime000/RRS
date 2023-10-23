package com.space000.dto;

import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

@Data
public class FileDto { //테스트
    private static ModelMapper modelMapper;

    private Long id;
    private String fileName;

    @NotBlank
    private String oriFileName;
    private String fileUrl;

    public static FileDto of(Object o) {
        return modelMapper.map(o, FileDto.class);
    }
}
