package com.space000.service;

import com.space000.entity.ReviewFile;
import com.space000.repository.ReviewFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewFileService {

    @Value("${location}")
    private String location;

    private final FileService fileService;
    private final ReviewFileRepository reviewFileRepository;

    public void saveReviewFile(ReviewFile reviewFile, MultipartFile multipartFile) throws IOException {
        String oriFileName = multipartFile.getOriginalFilename();
        String fileName = fileService.uploadFile(location + "/review", oriFileName, multipartFile.getBytes());
        String fileUrl = "/files/review/" + fileName;
        reviewFile.setFileName(fileName);
        reviewFile.setOriFileName(oriFileName);
        reviewFile.setFileUrl(fileUrl);
    }

    public void deleteReviewFile(Long reviewFileId, String email) throws Exception {
        ReviewFile savedReviewFile = reviewFileRepository.findById(reviewFileId).orElseThrow(EntityNotFoundException::new);
        String createBy = savedReviewFile.getReview().getCreateBy();

        if (!createBy.equals(email))
            throw new AccessDeniedException("해당 권한이 없음.");

        fileService.deleteFile(fileService.replaceUrl(savedReviewFile.getFileUrl()));
        reviewFileRepository.delete(savedReviewFile);
    }

}
