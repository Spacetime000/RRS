package com.space000.service;

import com.space000.dto.ReviewBoardDto;
import com.space000.dto.ReviewDtlDto;
import com.space000.dto.ReviewFormDto;
import com.space000.entity.*;
import com.space000.repository.MemberRepository;
import com.space000.repository.ReviewRepository;
import com.space000.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;
    private final MemberRepository memberRepository;
    private final ReviewFileService reviewFileService;
    private final FileService fileService;

    public Long saveReview(ReviewFormDto reviewFormDto, List<MultipartFile> multipartFileList) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findByEmail(userDetails.getUsername());
        Review review = reviewFormDto.createReview();
        Shop shop = shopRepository.findById(reviewFormDto.getShopId())
                .orElseGet(() -> shopRepository.save(reviewFormDto.createShop()));

        for (MultipartFile multipartFile : multipartFileList) {
            ReviewFile reviewFile = new ReviewFile();
            reviewFileService.saveReviewFile(reviewFile, multipartFile);
            reviewFile.setReview(review);
        }

        review.setShop(shop);
        review.setMember(member);
        reviewRepository.save(review);
        return review.getId();
    }

    @Transactional(readOnly = true)
    public ReviewFormDto getReviewFormDto(Long reviewId, String email) { //email 현재 회원, 게시글 작성 유저 비교용
        Review review = reviewRepository.findById(reviewId).orElseThrow(EntityNotFoundException::new);
        log.info("review 값 체크 : {}", review.toString());

        ReviewFormDto reviewFormDto = ReviewFormDto.of(review);

        if (!review.getMember().getEmail().equals(email))
            throw new AccessDeniedException("해당 글 수정에 대한 권한이 없음.");

        return reviewFormDto;

    }

    //리뷰 글 상세 내용
    public ReviewDtlDto getReviewDtl(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(EntityNotFoundException::new);
        ReviewDtlDto reviewDtlDto = ReviewDtlDto.of(review);
        return reviewDtlDto;

    }

    public void updateReview(ReviewFormDto reviewFormDto, List<MultipartFile> multipartFileList, String email) throws IOException {
        Review review = reviewRepository.findById(reviewFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        if (!review.getCreateBy().equals(email)) {
            throw new AccessDeniedException("해당 글 수정에 대한 권한이 없음.");
        }

        review.setTitle(reviewFormDto.getTitle());
        review.setContent(reviewFormDto.getContent());
        review.setRating(reviewFormDto.getRating());

        if (!multipartFileList.get(0).isEmpty()) {
            for (MultipartFile multipartFile : multipartFileList) {
                ReviewFile reviewFile = new ReviewFile();
                reviewFileService.saveReviewFile(reviewFile, multipartFile);
                reviewFile.setReview(review);
            }
        }

    }

    public void deleteReview(String email, Long reviewId) throws Exception {
        Review review = reviewRepository.findById(reviewId).orElseThrow(EntityNotFoundException::new);

        if (!review.getCreateBy().equals(email)) {
            throw new AccessDeniedException("해당 글 수정에 대한 권한이 없음.");
        }

        for (ReviewFile reviewFile : review.getReviewFileList()) {
            fileService.deleteFile(fileService.replaceUrl(reviewFile.getFileUrl()));
        }

        reviewRepository.delete(review);
    }

    public Page<ReviewBoardDto> getReviewPage(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        Page<Review> pageReview = reviewRepository.findAllByOrderByCreateTimeDesc(pageRequest);
        Page<ReviewBoardDto> pageDto = pageReview.map(review -> new ReviewBoardDto(
                review.getId(), review.getShop().getCategory(),
                review.getShop().getName(), review.getTitle(),
                review.getRating(), review.getMember().getNickname(),
                review.getCreateTime()));
        return pageDto;
    }

}
