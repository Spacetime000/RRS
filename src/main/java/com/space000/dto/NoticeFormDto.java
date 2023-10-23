package com.space000.dto;

import com.space000.entity.Notice;
import lombok.Data;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class NoticeFormDto {
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private String content;

    private List<NoticeFileDto> noticeFileDtoList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public static NoticeFormDto of(Notice notice) {
        NoticeFormDto noticeFormDto = modelMapper.map(notice, NoticeFormDto.class);

        for (int i = 0; i < notice.getNoticeFiles().size(); i++) {
            noticeFormDto.getNoticeFileDtoList().add(NoticeFileDto.of(notice.getNoticeFiles().get(i)));
        }
        return noticeFormDto;
    }

    public Notice createNotice() {
        return modelMapper.map(this, Notice.class);
    }

}
