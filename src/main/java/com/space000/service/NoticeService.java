package com.space000.service;

import com.space000.dto.NoticeBoardDto;
import com.space000.dto.NoticeFileDto;
import com.space000.dto.NoticeFormDto;
import com.space000.entity.Notice;
import com.space000.entity.NoticeFile;
import com.space000.repository.NoticeFileRepository;
import com.space000.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileService fileService;
    private final NoticeFileService noticeFileService;

    /**
     * 공지사항 작성 저장
     */
    public Long saveNotice(NoticeFormDto noticeFormDto, List<MultipartFile> multipartFileList) throws EntityNotFoundException, IOException {
        Notice notice = noticeFormDto.createNotice();

        if (!multipartFileList.get(0).isEmpty()) {
            for (MultipartFile multipartFile : multipartFileList) {
                NoticeFile noticeFile = new NoticeFile();
                noticeFileService.saveNoticeFile(noticeFile, multipartFile);
                noticeFile.setNotice(notice);
            }
        }

        noticeRepository.save(notice);
        return notice.getId();
    }

    public Long updateNotice(NoticeFormDto noticeFormDto, List<MultipartFile> multipartFileList) throws EntityNotFoundException, IOException {
        Notice notice = noticeRepository.findById(noticeFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        notice.update(noticeFormDto.getTitle(), noticeFormDto.getContent());

        if (!multipartFileList.get(0).isEmpty()) {

            for (MultipartFile multipartFile : multipartFileList) {
                NoticeFile noticeFile = new NoticeFile();
                noticeFileService.saveNoticeFile(noticeFile, multipartFile);
                noticeFile.setNotice(notice);
            }
        }
        noticeRepository.save(notice);
        return notice.getId();
    }

    @Transactional(readOnly = true)
    public NoticeFormDto getNoticeFormDto(Long noticeId) throws EntityNotFoundException {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
        NoticeFormDto noticeFormDto = NoticeFormDto.of(notice);
        log.info("첨부파일 size : {}", noticeFormDto.getNoticeFileDtoList().size());
        return noticeFormDto;

    }

    @Transactional(readOnly = true)
    public List<NoticeBoardDto> getNoticeBoardDto() {
        List<Notice> noticeList = noticeRepository.findByOrderByCreateTimeDesc();
        List<NoticeBoardDto> noticeBoardDtoList = new ArrayList<>();

        for (Notice notice : noticeList) {
            noticeBoardDtoList.add(NoticeBoardDto.of(notice));
        }

        return noticeBoardDtoList;
    }

    public void deleteNotice(Long noticeId) throws Exception {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);

        for (NoticeFile noticeFile : notice.getNoticeFiles()) {
            fileService.deleteFile(fileService.replaceUrl(noticeFile.getFileUrl()));
        }
        noticeRepository.delete(notice);
    }
}
