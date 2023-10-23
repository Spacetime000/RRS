package com.space000.dto;

import com.space000.entity.NoticeFile;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class NoticeFileDto {

    private Long id;
    private String fileName;
    private String oriFileName;
    private String fileUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static NoticeFileDto of(NoticeFile noticeFile) {
        return modelMapper.map(noticeFile, NoticeFileDto.class);
    }

}
