package com.space000.dto;

import com.space000.entity.Notice;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Data
public class NoticeBoardDto {
    private Long id;
    private String title;
    private LocalDateTime createTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public static NoticeBoardDto of(Notice notice) {
        NoticeBoardDto noticeBoardDto = modelMapper.map(notice, NoticeBoardDto.class);
        return noticeBoardDto;
    }
}
