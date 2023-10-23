package com.space000.service;

import com.space000.entity.NoticeFile;
import com.space000.repository.NoticeFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NoticeFileService {

    @Value("${location}")
    private String location;

    private final NoticeFileRepository noticeFileRepository;
    private final FileService fileService;

    public void saveNoticeFile(NoticeFile noticeFile, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            log.info("저장할 파일이 없습니다.");
            return;
        }

        String oriFileName = multipartFile.getOriginalFilename();
        String fileName = fileService.uploadFile(location + "/notice", oriFileName, multipartFile.getBytes()); //uuid
        String fileUrl = "/files/notice/" + fileName;
        noticeFile.setFileName(fileName);
        noticeFile.setOriFileName(oriFileName);
        noticeFile.setFileUrl(fileUrl);
    }

    public void deleteNoticeFile(Long noticeFileId) throws Exception {
        NoticeFile savedNoticeFile = noticeFileRepository.findById(noticeFileId).orElseThrow(EntityNotFoundException::new);
        fileService.deleteFile(fileService.replaceUrl(savedNoticeFile.getFileUrl()));
        noticeFileRepository.delete(savedNoticeFile);
    }

    //파일다운로드
    public ResponseEntity<Resource> downFile(Long noticeFileId) throws MalformedURLException {
        NoticeFile savedNoticeFile = noticeFileRepository.findById(noticeFileId).orElseThrow(EntityNotFoundException::new);
        UrlResource resource = new UrlResource("file", fileService.replaceUrl(savedNoticeFile.getFileUrl()));
        String encodedFileName = UriUtils.encode(savedNoticeFile.getOriFileName(), StandardCharsets.UTF_8);
        String content = "attachment; filename=\"" + encodedFileName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, content).body(resource);
    }

}
