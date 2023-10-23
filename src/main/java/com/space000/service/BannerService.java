package com.space000.service;

import com.space000.dto.BannerDto;
import com.space000.dto.MainBannerDto;
import com.space000.entity.Banner;
import com.space000.repository.BannerRepository;
import com.space000.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BannerService {

    @Value("${location}")
    private String location;

    private final FileService fileService;
    private final BannerRepository bannerRepository;

    public void saveBanner(String bannerState, MultipartFile multipartFile) throws IOException {
        String fileName = fileService.uploadFile(location + "/banner", multipartFile.getOriginalFilename(), multipartFile.getBytes());
        Banner banner = new Banner();
        banner.setFileName(fileName);
        banner.setState(bannerState);
        banner.setFileUrl("/files/banner/" + fileName);
        banner.setOriFileName(multipartFile.getOriginalFilename());
        bannerRepository.save(banner);
    }

    public void updateBanner(String bannerState, Long id, MultipartFile multipartFile) throws Exception {
        Banner banner = bannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        fileService.deleteFile(fileService.replaceUrl(banner.getFileUrl()));
        String fileName = fileService.uploadFile(location + "/banner", multipartFile.getOriginalFilename(), multipartFile.getBytes());
        banner.setFileName(fileName);
        banner.setState(bannerState);
        banner.setFileUrl("/files/banner/" + fileName);
        banner.setOriFileName(multipartFile.getOriginalFilename());
    }

    public void updateBannerState(String bannerState, Long id) {
        Banner banner = bannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        banner.setState(bannerState);
    }

    public void deleteBanner(Long id) throws Exception {
        Banner banner = bannerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        fileService.deleteFile(fileService.replaceUrl(banner.getFileUrl()));
        bannerRepository.delete(banner);
    }

    @Transactional(readOnly = true)
    public List<BannerDto> getBannerManagement() {
        List<Banner> all = bannerRepository.findAll();
        List<BannerDto> bannerDtoList = new ArrayList<>();

        for (Banner banner : all) {
            bannerDtoList.add(BannerDto.of(banner));
        }

        return bannerDtoList;
    }

}
